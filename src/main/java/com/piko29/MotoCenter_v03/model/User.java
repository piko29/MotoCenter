package com.piko29.MotoCenter_v03.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "application_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			//added 06.12
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	)
	private Set<UserRole> roles = new HashSet<>();
	//added 06.12 to check motoproduct by userid, currently unused, to check!
	@OneToMany(mappedBy = "user")
	private List<MotoProduct> motoProductList = new ArrayList<>();

	//04.01 reading messages
	@OneToMany(mappedBy = "user")
	private List<Message> messageList = new ArrayList<>();
}
