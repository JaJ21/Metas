package br.com.empresa.metas.meta.infra.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "meta_valor", uniqueConstraints = {
        @UniqueConstraint(name = "uk_meta_valor_meta_ano_mes", columnNames = {"meta_id", "ano", "mes"})
})
public class MetaValorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_meta_valor_meta"))
    private MetaEntity meta;

    // Colunas criadas como SMALLINT na migration V4 (ano cabe folgado e
    // mês é 1..12). O tipo Java é short justamente pra casar com SMALLINT
    // no schema-validation do Hibernate (int mapearia pra INTEGER).
    @Column(name = "ano", nullable = false)
    private short ano;

    @Column(name = "mes", nullable = false)
    private short mes;

    /** Guardado como texto ("REAL"/"FORECAST"/"ORCADO") — ver br.com.empresa.metas.meta.core.domain.enums.TipoValorMes. */
    @Column(name = "tipo", nullable = false, length = 10)
    private String tipo;

    @Column(name = "valor", nullable = false, precision = 18, scale = 2)
    private BigDecimal valor;

    protected MetaValorEntity() {}

    public MetaValorEntity(Long id, MetaEntity meta, short ano, short mes, String tipo, BigDecimal valor) {
        this.id = id;
        this.meta = meta;
        this.ano = ano;
        this.mes = mes;
        this.tipo = tipo;
        this.valor = valor;
    }

    public Long getId() { return id; }
    public MetaEntity getMeta() { return meta; }
    public short getAno() { return ano; }
    public short getMes() { return mes; }
    public String getTipo() { return tipo; }
    public BigDecimal getValor() { return valor; }
}
