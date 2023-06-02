package com.TenniSchool.tenniSchool.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @Column(name = "schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedule_id;

    private Date wantDate;
    private int openTime;
    private int endTime;
    private Boolean canReservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tennis_id")
    private TennisPlace tennisPlace;


}
