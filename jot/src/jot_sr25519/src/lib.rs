use jni::objects::{JByteArray, JClass, JObject, JObjectArray, JString};
use jni::sys::{jbyteArray};
use jni::JNIEnv;
use sp_core::Pair;

/// Convert Java byte array to Rust Vec<u8>
fn jbytearray_to_vec(env: &JNIEnv, input: JByteArray) -> jni::errors::Result<Vec<u8>> {
    let len = env.get_array_length(&input)?;
    let mut buffer = vec![0i8; len as usize];
    env.get_byte_array_region(&input, 0, &mut buffer)?;
    Ok(buffer.iter().map(|b| *b as u8).collect())
}

/// Convert Rust &[u8] to Java byte array
fn vec_to_jbytearray<'a>(env: &'a JNIEnv<'a>, data: &'a [u8]) -> jni::errors::Result<JByteArray<'a>> {
    let array = env.new_byte_array(data.len() as i32)?;
    let i8_slice: Vec<i8> = data.iter().map(|b| *b as i8).collect();
    env.set_byte_array_region(&array, 0, &i8_slice)?;
    Ok(array)
}

#[no_mangle]
pub extern "system" fn Java_com_method5_jot_crypto_Sr25519_nativeSign(
    env: JNIEnv,
    _class: JClass,
    seed: JByteArray,
    message: JByteArray,
) -> jbyteArray {
    let seed_vec = match jbytearray_to_vec(&env, seed) {
        Ok(v) => v,
        Err(_) => return env.new_byte_array(0).unwrap().as_raw(),
    };
    let message_vec = match jbytearray_to_vec(&env, message) {
        Ok(v) => v,
        Err(_) => return env.new_byte_array(0).unwrap().as_raw(),
    };

    let pair = match sp_core::sr25519::Pair::from_seed_slice(&seed_vec) {
        Ok(p) => p,
        Err(_) => return env.new_byte_array(0).unwrap().as_raw(),
    };

    let sig = pair.sign(&message_vec);

    match vec_to_jbytearray(&env, &sig.as_ref()) {
        Ok(arr) => arr.as_raw(),
        Err(_) => env.new_byte_array(0).unwrap().into_raw(),
    }
}

#[no_mangle]
pub extern "system" fn Java_com_method5_jot_crypto_Sr25519_nativeVerify(
    env: JNIEnv,
    _class: JClass,
    public_key: JByteArray,
    message: JByteArray,
    signature: JByteArray,
) -> i32 {
    let pub_bytes = match jbytearray_to_vec(&env, public_key) {
        Ok(v) => v,
        Err(_) => return 0,
    };
    let msg = match jbytearray_to_vec(&env, message) {
        Ok(v) => v,
        Err(_) => return 0,
    };
    let sig_bytes = match jbytearray_to_vec(&env, signature) {
        Ok(v) => v,
        Err(_) => return 0,
    };

    let sig = match sp_core::sr25519::Signature::try_from(&sig_bytes[..]) {
        Ok(s) => s,
        Err(_) => return 0,
    };

    let pubkey = match sp_core::sr25519::Public::try_from(&pub_bytes[..]) {
        Ok(p) => p,
        Err(_) => return 0,
    };

    if sp_core::sr25519::Pair::verify(&sig, &msg, &pubkey) {
        1
    } else {
        0
    }
}

#[no_mangle]
pub extern "system" fn Java_com_method5_jot_crypto_Sr25519_nativeDerivePublicKey(
    env: JNIEnv,
    _class: JClass,
    seed: jbyteArray,
) -> jbyteArray {
    // Convert raw jbyteArray into JNI-safe wrapper
    let seed_array = unsafe { JByteArray::from_raw(seed) };

    let seed_bytes = match jbytearray_to_vec(&env, seed_array) {
        Ok(bytes) => bytes,
        Err(_) => return env.new_byte_array(0).unwrap().into_raw(),
    };

    if seed_bytes.len() != 32 {
        return env.new_byte_array(0).unwrap().into_raw();
    }

    let mut seed_fixed = [0u8; 32];
    seed_fixed.copy_from_slice(&seed_bytes);

    let pair = sp_core::sr25519::Pair::from_seed(&seed_fixed);
    let public = pair.public().0; // Raw [u8; 32]

    vec_to_jbytearray(&env, &public).unwrap_or_else(|_| env.new_byte_array(0).unwrap()).into_raw()
}

#[no_mangle]

pub extern "system" fn Java_com_method5_jot_crypto_Sr25519_nativeDeriveSeedFromMnemonic(
    mut env: JNIEnv,
    _class: JClass,
    mnemonic: JString,
    password: JString,
) -> jbyteArray {
    let mnemonic_str: String = match env.get_string(&mnemonic) {
        Ok(s) => s.into(),
        Err(_) => return env.new_byte_array(0).unwrap().into_raw(),
    };
    let password_str: String = match env.get_string(&password) {
        Ok(s) => s.into(),
        Err(_) => return env.new_byte_array(0).unwrap().into_raw(),
    };

    let password_opt = if password_str.trim().is_empty() {
        None
    } else {
        Some(password_str.as_str())
    };

    let (_pair, seed) = match sp_core::sr25519::Pair::from_phrase(&mnemonic_str, password_opt) {
        Ok(res) => res,
        Err(_) => return env.new_byte_array(0).unwrap().into_raw(),
    };

    let seed32 = &seed[..32]; // get first 32 bytes (MiniSecretKey format)

    match vec_to_jbytearray(&env, seed32) {
        Ok(arr) => arr.into_raw(),
        Err(_) => env.new_byte_array(0).unwrap().into_raw(),
    }
}

#[no_mangle]
pub extern "system" fn Java_com_method5_jot_crypto_Sr25519_nativeGenerateMnemonicAndSeed<'a>(
    mut env: JNIEnv<'a>,
    _: JClass<'a>,
) -> JObjectArray<'a> {
    let (_pair, phrase, seed) = sp_core::sr25519::Pair::generate_with_phrase(None);

    let seed = &seed[..32];

    let phrase_bytes = env
        .byte_array_from_slice(phrase.as_bytes())
        .unwrap();
    let seed_bytes = env
        .byte_array_from_slice(&seed[..32])
        .unwrap();

    let arr = env
        .new_object_array(2, "[B", JObject::null())
        .unwrap();

    env.set_object_array_element(&arr, 0, phrase_bytes).unwrap();
    env.set_object_array_element(&arr, 1, seed_bytes).unwrap();

    arr
}