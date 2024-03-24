package dev.projectfitness.ipfitness.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "fitness_user")
public class FitnessUserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "UserId", nullable = false)
    private Integer userId;
    @Basic
    @Column(name = "Username", nullable = false, unique = true, length = 64)
    private String username;
    @Basic
    @Column(name = "Password", nullable = false, length = 256)
    private String password;
    @Basic
    @Column(name = "FirstName", nullable = false, length = 64)
    private String firstName;
    @Basic
    @Column(name = "LastName", nullable = false, length = 96)
    private String lastName;
    @Basic
    @Column(name = "Mail", nullable = false, length = 128)
    private String mail;
    @Basic
    @Column(name = "City", nullable = false, length = 128)
    private String city;
    @Basic
    @Column(name = "Avatar", nullable = true, length = 128)
    private String avatar;
    @Basic
    @Column(name = "Biography", nullable = false, length = 512)
    private String biography;
    @Basic
    @Column(name = "ContactInfo", nullable = false, length = 128)
    private String contactInfo;
    @Basic
    @Column(name = "Active", nullable = false)
    private Boolean active;
    @Basic
    @Column(name = "Enabled", nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "activityId")
    private List<ActivityEntity> activities;
    @OneToMany(mappedBy = "programId")
    private List<FitnessProgramEntity> fitnessPrograms;
    @OneToMany(mappedBy = "userFromId")
    private List<MessageEntity> messages;

    @ManyToMany
    @JoinTable(name = "category_subscription",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "categoryId")}
    )
    private Set<CategoryEntity> subscribedCategories;

}
