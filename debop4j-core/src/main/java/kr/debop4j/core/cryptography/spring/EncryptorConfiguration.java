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

package kr.debop4j.core.cryptography.spring;

import kr.debop4j.core.cryptography.disgest.*;
import kr.debop4j.core.cryptography.symmetric.DESByteEncryptor;
import kr.debop4j.core.cryptography.symmetric.RC2ByteEncryptor;
import kr.debop4j.core.cryptography.symmetric.TripleDESByteEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Encriptor 를 구현한 클래스들을 Springs Bean 으로 제공하는 Anntated Configuration 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 17
 */
@Configuration
public class EncryptorConfiguration {

    /**
     * Md 5 string digester.
     *
     * @return the mD 5 string digester
     */
    @Bean(name = "md5StringDigester")
    public MD5StringDigester md5StringDigester() {
        return new MD5StringDigester();
    }

    /**
     * Sha 1 string digester.
     *
     * @return the sHA 1 string digester
     */
    @Bean(name = "sha1StringDigester")
    public SHA1StringDigester sha1StringDigester() {
        return new SHA1StringDigester();
    }

    /**
     * Sha 256 string digester.
     *
     * @return the sHA 256 string digester
     */
    @Bean(name = "sha256StringDigester")
    public SHA256StringDigester sha256StringDigester() {
        return new SHA256StringDigester();
    }

    /**
     * Sha 384 string digester.
     *
     * @return the sHA 384 string digester
     */
    @Bean(name = "sha384StringDigester")
    public SHA384StringDigester sha384StringDigester() {
        return new SHA384StringDigester();
    }

    /**
     * Sha 512 string digester.
     *
     * @return the sHA 512 string digester
     */
    @Bean(name = "sha512StringDigester")
    public SHA512StringDigester sha512StringDigester() {
        return new SHA512StringDigester();
    }


    /**
     * Des byte encryptor.
     *
     * @return the dES byte encryptor
     */
    @Bean(name = "desByteEncryptor")
    public DESByteEncryptor desByteEncryptor() {
        return new DESByteEncryptor();
    }

    /**
     * Triple dES byte encryptor.
     *
     * @return the triple dES byte encryptor
     */
    @Bean(name = "tripleByteEncryptor")
    public TripleDESByteEncryptor tripleDESByteEncryptor() {
        return new TripleDESByteEncryptor();
    }

    /**
     * Rc 2 byte encryptor.
     *
     * @return the rC 2 byte encryptor
     */
    @Bean(name = "rc2ByteEncryptor")
    public RC2ByteEncryptor rc2ByteEncryptor() {
        return new RC2ByteEncryptor();
    }
}
