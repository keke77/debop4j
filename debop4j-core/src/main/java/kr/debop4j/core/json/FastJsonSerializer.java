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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

/**
 * FastJsonSerializer
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 15. 오전 12:57
 */
@Slf4j
public class FastJsonSerializer implements IJsonSerializer {

    private SerializerFeature[] features = new SerializerFeature[]{
            SerializerFeature.UseISO8601DateFormat,
            SerializerFeature.WriteClassName
    };

    @Override
    public String serializeToText(Object graph) {
        if (graph == null)
            return "";
        if (log.isTraceEnabled())
            log.trace("지정한 객체를 JSON 직렬화를 수행합니다. graph=[{}]", graph);
        return JSON.toJSONString(graph, features);
    }

    @Override
    public <T> T deserializeFromText(String jsonText, Class<T> targetClass) {
        if (log.isTraceEnabled())
            log.trace("Json 문자열을 역직렬화하여 지정한 클래스롤 빌드합니다. targetClass=[{}]", targetClass.getName());
        return JSON.parseObject(jsonText, targetClass, Feature.AllowISO8601DateFormat);
    }

    @Override
    public byte[] serialize(Object graph) {
        if (graph == null)
            return EMPTY_BYTES;

        if (log.isTraceEnabled())
            log.trace("지정한 객체를 JSON 직렬화를 수행합니다. graph=[{}]", graph);

        return JSON.toJSONBytes(graph, features);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (log.isTraceEnabled())
            log.trace("Json 배열을 역직렬화하여 지정한 클래스롤 빌드합니다. clazz=[{}]", clazz.getName());
        return JSON.parseObject(bytes, clazz, Feature.AllowISO8601DateFormat);
    }
}
