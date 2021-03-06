package kr.debop4j.core.cryptography;

import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 암호화 관련 Helper Class
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 14
 */
@Slf4j
public abstract class CryptoTool {

    private static final String RANDOM_NUMBER_GENERATION = "SHA1PRNG";
    private static final SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstance(RANDOM_NUMBER_GENERATION);
        } catch (NoSuchAlgorithmException e) {
            log.error("해당 난수 발생 알고리즘을 찾을 수 없습니다. algorithm=" + RANDOM_NUMBER_GENERATION);
            throw new RuntimeException(e);
        }
    }

    private CryptoTool() { }

    /**
     * 난수를 byte array 에 채웁니다.
     *
     * @param numBytes 바이트 수
     * @return 난수가 채워진 byte array
     */
    public static byte[] getRandomBytes(final int numBytes) {
        assert (numBytes >= 0);
        byte[] bytes = new byte[numBytes];
        if (numBytes > 0)
            random.nextBytes(bytes);
        return bytes;
    }
}
