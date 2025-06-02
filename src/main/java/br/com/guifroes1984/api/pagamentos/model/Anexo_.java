package br.com.guifroes1984.api.pagamentos.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Anexo.class)
public abstract class Anexo_ {

    public static volatile SingularAttribute<Anexo, Long> codigo;
    public static volatile SingularAttribute<Anexo, String> nome;
    public static volatile SingularAttribute<Anexo, String> tipo;
    public static volatile SingularAttribute<Anexo, byte[]> dados;
}
