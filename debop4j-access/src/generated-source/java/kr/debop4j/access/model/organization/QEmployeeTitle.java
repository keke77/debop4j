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

package kr.debop4j.access.model.organization;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QEmployeeTitle is a Querydsl query type for EmployeeTitle */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEmployeeTitle extends EntityPathBase<EmployeeTitle> {

    private static final long serialVersionUID = 41227585;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmployeeTitle employeeTitle = new QEmployeeTitle("employeeTitle");

    public final QEmployeeCodeBase _super;

    //inherited
    public final StringPath code;

    // inherited
    public final QCompany company;

    //inherited
    public final StringPath description;

    //inherited
    public final StringPath exAttr;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath name;

    //inherited
    public final BooleanPath persisted;

    //inherited
    public final NumberPath<Integer> viewOrder;

    public QEmployeeTitle(String variable) {
        this(EmployeeTitle.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QEmployeeTitle(Path<? extends EmployeeTitle> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployeeTitle(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEmployeeTitle(PathMetadata<?> metadata, PathInits inits) {
        this(EmployeeTitle.class, metadata, inits);
    }

    public QEmployeeTitle(Class<? extends EmployeeTitle> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QEmployeeCodeBase(type, metadata, inits);
        this.code = _super.code;
        this.company = _super.company;
        this.description = _super.description;
        this.exAttr = _super.exAttr;
        this.name = _super.name;
        this.persisted = _super.persisted;
        this.viewOrder = _super.viewOrder;
    }

}
