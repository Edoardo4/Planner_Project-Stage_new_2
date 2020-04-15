package it.cgm.planner.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.MappedSuperclass;


@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdBy", "updatedBy"},
        allowGetters = true
)
@Getter @Setter
public abstract class UserDateAudit extends DateAudit {

	private static final long serialVersionUID = 1L;

	@CreatedBy
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;
}
