package formstart.form.domain.property;

import lombok.Data;

@Data
public class DeliveryCode {

    String code;
    String displayName;

    public DeliveryCode(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * 배송 방식은 `DeliveryCode` 라는 클래스를 사용한다. `code` 는 `FAST` 같은 시스템에서 전달하는 값이고,
     * `displayName` 은 `빠른 배송` 같은 고객에게 보여주는 값이다.
     */
}
