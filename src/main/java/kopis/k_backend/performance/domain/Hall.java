package kopis.k_backend.performance.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sidonm;

    @Column
    private String gugunnm;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String hallName;

    @Column(unique = true)
    private String kopisHallId;

    @Column
    private String parkinglot;

    @Column
    private String restaurant;

    @Column
    private String cafe;

    @Column
    private String store;

    @Column
    private String nolibang;

    @Column
    private String suyu;

    @Column
    private String telno;

    @Column
    private String relateurl;

    public void updateExistingHall(String streetAddress, String restaurant, String cafe, String store, String nolibang, String suyu, String parkinglot, String telno, String relateurl){
        this.streetAddress = streetAddress;
        this.restaurant = restaurant;
        this.cafe = cafe;
        this.store = store;
        this.nolibang = nolibang;
        this.suyu = suyu;
        this.parkinglot = parkinglot;
        this.telno = telno;
        this.relateurl = relateurl;
    }
}