package com.method5.jot.metadata;

import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.entity.metadata.MetadataPallet;
import com.method5.jot.entity.metadata.MetadataType;
import com.method5.jot.entity.metadata.MetadataTypeVariants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallIndexResolver {
    private final Map<String, byte[]> callMap = new HashMap<>();
    private final Map<Integer, String> moduleIndexToName = new HashMap<>();
    private final Map<String, List<String>> moduleFunctions = new HashMap<>();
    private final Map<String, List<String>> moduleEvents = new HashMap<>();
    private final Map<String, Map<Integer, String>> moduleErrors = new HashMap<>();

    public CallIndexResolver() {}

    public CallIndexResolver(MetadataV14 metadata) {
        initialize(metadata);
    }

    public void initialize(MetadataV14 metadata) {
        List<MetadataPallet> pallets = metadata.getPallets();
        List<MetadataType> types = metadata.getTypes();

        for (MetadataPallet pallet : pallets) {
            String palletName = pallet.getName();

            if (pallet.getCall() != null) {
                List<MetadataTypeVariants.Variant> callVariants = resolveVariants(types, pallet.getCall().getType());
                if(!callVariants.isEmpty()) {
                    for (MetadataTypeVariants.Variant variant : callVariants) {
                        registerCall(palletName, variant.getName(), new byte[]{(byte) pallet.getIndex(), (byte) variant.getIndex()});
                    }
                } else {
                    moduleIndexToName.put(pallet.getIndex() & 0xFF, pallet.getName());
                }
            } else {
                moduleIndexToName.put(pallet.getIndex() & 0xFF, pallet.getName());
            }

            if (pallet.getErrors() != null) {
                List<MetadataTypeVariants.Variant> errorVariants = resolveVariants(types, pallet.getErrors().getType());
                for (MetadataTypeVariants.Variant variant : errorVariants) {
                    registerError(palletName, variant.getIndex(), variant.getName());
                }
            }

            if (pallet.getEvent() != null) {
                List<MetadataTypeVariants.Variant> eventVariants = resolveVariants(types, pallet.getEvent().getType());
                for (MetadataTypeVariants.Variant variant : eventVariants) {
                    moduleEvents.computeIfAbsent(palletName, k -> new ArrayList<>()).add(variant.getName());
                }
            }
        }
    }

    public void clear() {
        callMap.clear();
        moduleIndexToName.clear();
        moduleFunctions.clear();
        moduleErrors.clear();
    }

    public void registerCall(String pallet, String method, byte[] index) {
        callMap.put(pallet + "." + method, index);
        moduleIndexToName.put((int) index[0] & 0xFF, pallet);
        moduleFunctions.computeIfAbsent(pallet, k -> new ArrayList<>()).add(method);
    }

    public byte[] resolveCallIndex(String pallet, String method) {
        return callMap.getOrDefault(pallet + "." + method, null);
    }

    public void registerError(String pallet, int errorCode, String description) {
        moduleErrors.computeIfAbsent(pallet, k -> new HashMap<>()).put(errorCode, description);
    }

    public String getModuleName(int moduleIndex) {
        return moduleIndexToName.getOrDefault(moduleIndex, null);
    }

    public String getFunctionName(String module, int functionIndex) {
        return moduleFunctions.getOrDefault(module, new ArrayList<>()).get(functionIndex);
    }

    public String getModuleError(int moduleIndex, int errorCode) {
        String module = getModuleName(moduleIndex);
        if (module == null || module.equals("Unknown")) return null;

        Map<Integer, String> errors = moduleErrors.get(module);
        if (errors == null) return "No errors registered for module " + module;

        return errors.getOrDefault(errorCode, "Unknown error");
    }

    public String getEventName(int moduleIndex, int eventIndex) {
        String module = getModuleName(moduleIndex);
        if (module == null) return "UnknownModule";

        List<String> events = moduleEvents.get(module);
        if (events == null || eventIndex >= events.size()) return "UnknownEvent";

        return events.get(eventIndex);
    }

    private List<MetadataTypeVariants.Variant> resolveVariants(List<MetadataType> types, Integer typeId) {
        if (typeId == null) return List.of();
        for (MetadataType type : types) {
            if (type.getId() == typeId && type.getDef() instanceof MetadataTypeVariants) {
                return ((MetadataTypeVariants)type.getDef()).getVariants();
            }
        }
        return List.of();
    }
}
