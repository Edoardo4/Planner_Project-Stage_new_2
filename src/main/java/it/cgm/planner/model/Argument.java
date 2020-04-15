package it.cgm.planner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "argument", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "name"
        }),
        @UniqueConstraint(columnNames = {
                "code"
            })
})
public class Argument {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name")
	@NotNull @NotBlank
	@Size(max = 40)
	private String name;
	
	@Column(name="code")
	@NotNull @NotBlank
	@Size(max = 5)
	private String code;
	
	@Column(name = "is_valid", nullable = false, columnDefinition = "tinyint(1)")
	private boolean isValid = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	
}