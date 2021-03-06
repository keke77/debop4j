package kr.debop4j.core.guava.eventbus.events;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * kr.debop4j.core.guava.eventbus.events.CreditPurchaseEvent
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 10.
 */
@Slf4j
@Getter
public class CreditPurchaseEvent extends PurchaseEvent {

    private String creditCardNumber;
    private String item;

    public CreditPurchaseEvent(long amount, String item, String creditCardNumber) {
        super(amount);
        this.item = item;
        this.creditCardNumber = creditCardNumber;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("amount", amount)
                .add("creditCartNumber", creditCardNumber)
                .add("item", item)
                .toString();
    }
}
