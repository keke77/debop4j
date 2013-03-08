package kr.debop4j.data.model;

import kr.debop4j.core.tools.MapperTool;
import kr.debop4j.core.tools.ReflectTool;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * kr.debop4j.data.model.AnnotatedLocaleEntityBase
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오전 11:37
 */
@MappedSuperclass
@Slf4j
public abstract class AnnotatedLocaleEntityBase<TLocaleValue extends ILocaleValue>
        extends AnnotatedEntityBase implements ILocaleEntity<TLocaleValue> {

    abstract public Map<Locale, TLocaleValue> getLocaleMap();

    @Transient
    private TLocaleValue defaultLocaleValue = null;

    /**
     * 기본 Locale 정보를 생성하여 제공합니다.
     */
    public final TLocaleValue getDefaultLocaleValue() {
        if (defaultLocaleValue == null) {
            try {
                defaultLocaleValue = createDefaultLocaleValue();
            } catch (Exception ex) {
                log.error("기본 LocaleValue를 얻는데 실패했습니다.", ex);
            }
        }
        return defaultLocaleValue;
    }

    protected TLocaleValue createDefaultLocaleValue() {
        try {
            Class<TLocaleValue> localeValueClass = ReflectTool.getGenericParameterType(this, 1);

            if (log.isDebugEnabled())
                log.debug("기본 Locale 정보가 없습니다. 엔티티 [{}] 속성으로 기본 Locale [{}] 정보를 생성합니다...",
                          getClass().getName(), localeValueClass.getName());

            // ModelMapper가 엔티티의 속성 중 ILocaleValue 속성과 같은 것들에 대해 값을 복사한다.
            //
            return MapperTool.map(this, localeValueClass);

        } catch (Throwable t) {
            throw new IllegalStateException("기본 Locale 정보를 생성하는데 실패했습니다.", t);
        }
    }

    @Override
    public TLocaleValue getLocaleValue(Locale locale) {
        return getLocaleMap().get(locale);
    }

    @Override
    public Set<Locale> getLcoales() {
        return getLocaleMap().keySet();
    }

    @Override
    public void addLocaleValue(Locale locale, TLocaleValue localeValue) {
        getLocaleMap().put(locale, localeValue);
    }

    @Override
    public void removeLocaleValue(Locale locale) {
        getLocaleMap().remove(locale);
    }

    @Override
    public TLocaleValue getLocaleValueOrDefault(Locale locale) {
        if (getLocaleMap().size() == 0 || locale == null || locale.getDisplayName() == null)
            return getDefaultLocaleValue();

        if (getLocaleMap().containsKey(locale))
            return getLocaleMap().get(locale);

        return getDefaultLocaleValue();
    }

    @Override
    public TLocaleValue getCurrentLocaleValue() {
        return getLocaleValueOrDefault(Locale.getDefault());
    }
}
