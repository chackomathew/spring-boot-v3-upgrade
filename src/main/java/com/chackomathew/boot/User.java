package com.chackomathew.boot;

import org.hibernate.type.NumericBooleanConverter;
import org.hibernate.type.YesNoConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * User entity.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    
    private String lastName;

	@Convert(converter= YesNoConverter.class)
    private Boolean deleted;

    @Convert(converter= NumericBooleanConverter.class)
    private Boolean admin;

    public User(String firstName, String lastName, Boolean deleted, Boolean admin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.deleted = deleted;
        this.admin = admin;
      }
    
}
