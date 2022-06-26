package ru.shakurov.diploma.yandex_module.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity{

    @Id
    private String userId;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Token> tokens = new ArrayList<>();
}
