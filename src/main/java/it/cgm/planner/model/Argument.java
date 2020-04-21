package it.cgm.planner.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
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
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "argument_material",
	            joinColumns = @JoinColumn(name = "argument_id"),
	            inverseJoinColumns = @JoinColumn(name = "material_id"))
	 
    private Set<Material> materials = new HashSet<>();
	
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

	public Set<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(Set<Material> materials) {
		this.materials = materials;
	}
	
	//method for add material in a set 
	public void setMaterialAdd(Material material) {
		materials.add(material);
		 }
	//method for add collection of material in a set
	public void setMaterialAddCollection(Set<Material> material) {
		materials.addAll(material);
	 }
	//method for delete material in a set 
	public void setMaterialDel(Material material) {
		materials.remove(material);
	 }
	//method for delete all material  in a set 
	public void setMaterialDelCollection() {
		materials.removeAll(materials);
	 }
	//method for add List of material in a set
	public void setGradeAddCollection(List<Material> material) {
		materials.addAll(material);
	 }
}