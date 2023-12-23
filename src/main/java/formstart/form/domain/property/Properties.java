package formstart.form.domain.property;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class Properties {

    @Getter
    private static final LinkedHashMap<String, String> regions = new java.util.LinkedHashMap<>();
    @Getter
    private static final List<DeliveryCode> deliveryCodes = new ArrayList<>();

    static{
        initRegions();
        initDeliveryCodes();
    }

    private static void initRegions() {
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
    }

    private static void initDeliveryCodes() {
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
    }


}
