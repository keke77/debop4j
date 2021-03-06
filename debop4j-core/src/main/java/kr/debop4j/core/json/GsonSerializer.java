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

package kr.debop4j.core.json;

import com.google.gson.Gson;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.StringTool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * <a href="http://code.google.com/p/google-gson/">google-gson</a> 을 이용한 json serializer 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 14
 */
@Slf4j
public class GsonSerializer implements IJsonSerializer {

    @Getter
    private final Gson gson;

    /** Instantiates a new Gson serializer. */
    public GsonSerializer() {
        this(new Gson());
    }

    /**
     * Instantiates a new Gson serializer.
     *
     * @param gson the gson
     */
    public GsonSerializer(Gson gson) {
        this.gson = Guard.firstNotNull(gson, new Gson());
    }


    @Override
    public String serializeToText(Object graph) {
        if (graph == null)
            return "";

        if (log.isTraceEnabled())
            log.trace("Json 직렬화를 수행합니다... graph=[{}]", graph);

        return gson.toJson(graph);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> targetType) {
        return deserializeFromText(StringTool.getUtf8String(bytes), targetType);
    }

    @Override
    public <T> T deserializeFromText(String jsonText, Class<T> targetClass) {
        if (StringTool.isWhiteSpace(jsonText))
            return null;

        if (log.isTraceEnabled())
            log.trace("Json 역직렬화를 수행합니다. jsonText=[{}], targetClass=[{}]",
                      StringTool.ellipsisChar(jsonText, 255), targetClass);

        return gson.fromJson(jsonText, targetClass);
    }

    @Override
    public byte[] serialize(Object graph) {
        return StringTool.getUtf8Bytes(serializeToText(graph));
    }
}
