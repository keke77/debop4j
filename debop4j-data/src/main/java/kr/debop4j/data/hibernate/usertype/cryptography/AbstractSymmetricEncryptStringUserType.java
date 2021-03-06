/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.data.hibernate.usertype.cryptography;

import com.google.common.base.Objects;
import kr.debop4j.core.BinaryStringFormat;
import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;
import kr.debop4j.core.tools.StringTool;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 문자열을 암호화하여, HexDecimal 형태의 문자열로 변환하여 DB에 저장합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 18
 */
public abstract class AbstractSymmetricEncryptStringUserType implements UserType, Serializable {

    private static final Logger log = LoggerFactory.getLogger(AbstractSymmetricEncryptStringUserType.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();

    /**
     * Gets encryptor.
     *
     * @return the encryptor
     */
    abstract public ISymmetricByteEncryptor getEncryptor();

    /**
     * 문자열을 암호화하여, 16진법 문자열로 변환합니다.
     *
     * @param value the value
     * @return the string
     * @throws Exception the exception
     */
    protected String encrypt(final String value) throws Exception {
        if (value == null)
            return null;

        if (isTraceEnabled)
            log.trace("{}를 이용하여 문자열을 암호화합니다. value=[{}]", getEncryptor(), StringTool.ellipsisChar(value, 80));

        byte[] bytes = getEncryptor().encrypt(StringTool.getUtf8Bytes(value));
        return StringTool.getStringFromBytes(bytes, BinaryStringFormat.HexDecimal);
    }

    /**
     * 암호화된 16진법 문자열을 복원하여, 원래 문자열로 변환합니다.
     *
     * @param value the value
     * @return the string
     * @throws Exception the exception
     */
    protected String decrypt(final String value) throws Exception {
        if (value == null)
            return null;

        if (isTraceEnabled)
            log.trace("{}를 이용하여 암호화된 문자열을 복원합니다. value=[{}]",
                      getEncryptor(), StringTool.ellipsisChar(value, 80));

        byte[] bytes = getEncryptor().decrypt(StringTool.getBytesFromString(value, BinaryStringFormat.HexDecimal));
        return StringTool.getUtf8String(bytes);
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { StringType.INSTANCE.sqlType() };
    }

    @Override
    public Class returnedClass() {
        return String.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equal(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs,
                              String[] names,
                              SessionImplementor session,
                              Object owner) throws HibernateException,
            SQLException {
        try {
            String hexStr = StringType.INSTANCE.nullSafeGet(rs, names[0], session);
            return decrypt(hexStr);
        } catch (Exception ex) {
            throw new HibernateException("암호화를 복원하는데 실패했습니다.", ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st,
                            Object value,
                            int index,
                            SessionImplementor session) throws HibernateException,
            SQLException {
        try {
            StringType.INSTANCE.nullSafeSet(st, encrypt((String) value), index, session);
        } catch (Exception ex) {
            throw new HibernateException("암호화를 수행하는데 실패했습니다.", ex);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return StringType.INSTANCE.isMutable();
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

    private static final long serialVersionUID = -3643450762631678192L;
}
