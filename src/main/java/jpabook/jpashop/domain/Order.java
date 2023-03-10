package jpabook.jpashop.domain;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static org.hibernate.annotations.CascadeType.ALL;

@Entity
@Table(name = "ORDERS")
public class Order{


    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID", length = 10)
    private Long id;



    //1. < 'Order 객체의 필드 member(N)'와 'Member 객체의 필드 orders(1)' 간의 'N : 1 양방향 연관관계 매핑': 주문내역'목록' ID >
    //- '주인'이 'Order 객체의 필드 member(='Member 객체의 필드 orders')'인 경우
    //2. < 'Order 객체의 필드 member(N)'와 'Member 객체의 필드 id(1)' 간의 'N : 1 단방향 연관관계 매핑': 주문한 회원ID >
    //즉, 아래 'Order 객체의 필드 member'는, '양방향 매핑'과 '단방향 매핑' 둘 다 'Member 객체'의 각각 다른 필드들과 연결되어 있다
    //일단, 가급적 '단방향 매핑'을 제대로 해둔다! 이것만으로 '개체 설계'는 많이 됨.
    @ManyToOne(fetch = LAZY) //'주문(Order) 객체'와 '회원(Member) 객체'의 관계 = N : 1
                             //cf) 'fetch =...' 작성 후에 'alt + enter' 누르고,
                             //'add on demand static import for...'누르면, 더 축약되어 깔끔하게 써진다!
    @JoinColumn(name = "MEMBER_ID") //'주인인 현재 테이블 ORDER의 FK인 필드 member(= 'Member 객체의 필드 id')'는
                                    //'주인이 아닌 테이블 MEMBER의 PK인 컬럼 MEMBER_ID'에 대응된다!
    private Member member; //1. < N : 1 양방향 매핑 >
                           //- 'Order 객체'가 '외래키(필드 member)를 소유하고 있기에', '외래키 소유 객체인 Order 객체의 필드 member'가
                           //   양방향 매핑의 주인!
                           //- 'Member 객체' 입장에서는, '어떤 주문내역목록이 주문되었는지', '그 orders ID'에 대한 정보가 필요함
                           //   즉, 'orders ID'가 필요하고, 여기서의 'Member 객체의 필드 orders'가 바로 그 'oders ID'임.
                           //2. < N : 1 단방향 매핑 >
                           //- 'Order 객체의 필드 member'. FK = 'Member 객체의 필드 id'. PK
                           //   'Order 객체'의 입장에서는, '어떤 Member(회원)가 주문했는지' '그 Member ID'에 대한 정보가 필요함.
                           //   즉, 'member ID'가 필요하고, 여기서의 '필드 member'가 바로 그 'member ID'임.



    //1. < 'OrderItem 객체의 필드 order(N)'와 'Order 객체의 필드 orderItems(1)' 간의 'N : 1 양방향 연관관계 매핑' >
    //- '주인'이 'OrderItem 객체의 필드 order'인 경우
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
                                   //- '주문(Order) 객체'와 '주문상품(OrderItem) 객체'와의 관계 = 1 : N
                                   //- '반대편 연관관계이자 주인'인 'OrderItem 객체의 필드 order'와 '양방향 매핑'되어있다 라는 뜻
                                   //- '양방향 매핑의 주인인 OrderItem 객체의 필드 order'의 위에 어노테이션으로는
                                   //   'mappedBy'에 사용될 수 없기에
                                   //  '그 반대편 연관관계 매핑'의 대상인 이 '필드 orderItems'위에 'mappedBy' 되는 것이다!
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();
            //1. < 양방향 매핑 >
            //- 'OrderItems 객체'가 ''외래키(필드 order)'를 소유하고 있기에', '외래키 소유 객체인 OrderItems 객체의 필드 order'가
            //  양방향 매핑의 주인!
            //- 'Order 객체' 입장에서는, '어떤 상품들이 주문되었는지', '그 orderItems ID'에 대한 정보가 필요함
            //   즉, 'orderItems ID'가 필요하고, 여기서의 'Order 객체의 필드 orderItems'가 바로 그 'oderItems ID'임.
            //2. < 단방향 매핑 >
            //- 'Order 객체의 필드 id'. PK = 'OrderItems 객체의 필드 order'. FK


//    @Column(name = "MEMBER_ID")
//    private Long memberId; //그러나, 이 방식은 '객체지향스럽지 않다!'
//                           //이 방식은, '자바 객체 설계'를 'DB 테이블 설계'에 맞춘 방식이다.
//                           //왜냐하면, '테이블의 외래키를 자바 객체에 그대로 가져왔고',
//                           //         '객체 그래프 탐색이 불가능'하며,
//                           //         '참조가 없으므로 UML도 잘못됨'.


    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) //그러나, '1:1 양방향 매핑'에서는 꼭 반드시 주인객체('Order 객체') 내부에
                                                       //'Cascade'를 쓰는 것은 아니고,
                                                       //'주인 객체'와 '주인이 아닌 객체' 간의 '종속 관계를 고려하여
                                                       //'더 상위 객체(하위 객체를 관리하는(종속시키는) 객체)'에
                                                       //'Cascade'를 붙이는 것이ㅏㄷ.
                                                       //여기서는, 'Order 객체'와 'Delivery 객체' 관계에서,
                                                       //'주문 Order'이 발생해야, 그에 이어져서(종속적으로) '배송 Delivery'라는
                                                       //사건이 발생하므로, 'Order 객체'가 'Delivery 객체'를 종속하고,
                                                       //그에 따라, 'Order 객체 내부'에 'Cascade'를 붙이는 것이다!

    @JoinColumn(name = "DELIVERY_ID")//- '주인인 현재 테이블 ORDER의 FK인 필드 delivery(='Delivery 객체의 필드 id')'는
                                     //'주인이 아닌 테이블 DELIVERY의 PK인 컬럼 DELIVERY_ID'에 대응된다!
                                     //- '@JoinColumn'은 항상 '주인 객체의 내부 필드'에서 사용된다!
                                     //e.g) '@ManyToOne'과 '@JoinColumn'은 같이 사용됨
                                     //     '@OneToOne'과 '@JoinColumn'은 같이 사용됨
    private Delivery delivery;




    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) //enum 쓸 때는 반드시 'enumType.STRING'으로 해줘야 한다!
                                 //'enumType.ORDINAL'로 하면 절대 안된다!
    private OrderStatus status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
