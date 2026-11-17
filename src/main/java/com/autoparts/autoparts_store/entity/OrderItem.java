package com.autoparts.autoparts_store.entity;

// Импортируем аннотации для работы с базой данных.
import jakarta.persistence.*;

// BigDecimal используется для точного хранения денежных значений.
import java.math.BigDecimal;

// Указываем, что этот класс является сущностью базы данных.
@Entity

// Указываем название таблицы, в которой будут храниться товары заказов.
@Table(name = "order_items")
public class OrderItem {

    // Уникальный идентификатор записи о товаре в заказе.
    // Значение автоматически создаётся базой данных.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с заказом, к которому относится данный товар.
    // Несколько товаров могут относиться к одному заказу.
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Связь с товаром из каталога автозапчастей.
    // Один и тот же товар может встречаться в разных заказах.
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Количество единиц товара в заказе.
    // Например, если покупатель заказал 3 фильтра, здесь будет 3.
    @Column(nullable = false)
    private Integer quantity;

    // Цена одной единицы товара на момент оформления заказа.
    // Сохраняем цену отдельно, чтобы последующее изменение цены
    // в каталоге не повлияло на стоимость уже оформленного заказа.
    @Column(nullable = false)
    private BigDecimal unitPrice;

    // Пустой конструктор необходим для корректной работы JPA.
    public OrderItem() {
    }

    // Возвращает уникальный идентификатор записи.
    public Long getId() {
        return id;
    }

    // Возвращает заказ, к которому относится товар.
    public Order getOrder() {
        return order;
    }

    // Устанавливает заказ для данного товара.
    public void setOrder(Order order) {
        this.order = order;
    }

    // Возвращает информацию о товаре из каталога.
    public Product getProduct() {
        return product;
    }

    // Устанавливает товар, который входит в заказ.
    public void setProduct(Product product) {
        this.product = product;
    }

    // Возвращает количество заказанных единиц товара.
    public Integer getQuantity() {
        return quantity;
    }

    // Устанавливает количество заказанных единиц товара.
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Возвращает цену одной единицы товара на момент покупки.
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    // Устанавливает цену одной единицы товара.
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}