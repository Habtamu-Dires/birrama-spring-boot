package com.hab.birrama.members;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hab.birrama.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(
        name = "members"
)
public class Member {

    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1,
            initialValue = 1001
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "order_sequence"
    )
    @Column(
            name ="id"
    )
    private Long id;

    @Column(
            name = "first_name",
            nullable = false
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false
    )
    private String lastName;

    private String address;
    private String location;

   @OneToMany(
            mappedBy = "member"
    )
   @JsonIgnore
    private List<Order> orders = new ArrayList<>();


}
