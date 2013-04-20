package kr.debop4j.data.model;

import com.google.common.collect.Maps;
import lombok.Getter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 지역화 정보를 가지고, 메타 정보도 제공하는 엔티티의 추상 클래스
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 19
 */
@MappedSuperclass
public abstract class LocaleMetaEntityBase<TId extends Serializable, TLocaleValue extends ILocaleValue>
        extends LocaleEntityBase<TId, TLocaleValue> implements IMetaEntity {

    private static final long serialVersionUID = -3409892017189966160L;

    @Getter
    private Map<String, IMetaValue> metaMap = Maps.newLinkedHashMap();

    @Override
    public IMetaValue getMetaValue(String key) {
        return metaMap.get(key);
    }

    @Override
    public Set<String> getMetaKeys() {
        return metaMap.keySet();
    }

    @Override
    public void addMetaValue(String metaKey, IMetaValue metaValue) {
        metaMap.put(metaKey, metaValue);
    }

    @Override
    public void addMetaValue(String metaKey, Object value) {
        metaMap.put(metaKey, new SimpleMetaValue(value));
    }

    @Override
    public void removeMetaValue(String metaKey) {
        metaMap.remove(metaKey);
    }
}
