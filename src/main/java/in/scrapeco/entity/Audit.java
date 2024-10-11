package in.scrapeco.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class Audit implements Serializable {

	@CreatedDate
	@Column(name = "created_at", columnDefinition = "datetime default current_timestamp()")
	private OffsetDateTime createdAt = OffsetDateTime.now();

	@CreatedBy
	@Column(name = "created_by", length = 64)
	private String createdBy;

	@LastModifiedDate
	@Column(name = "updated_at", columnDefinition = "datetime ON UPDATE current_timestamp()")
	private OffsetDateTime updatedAt;

	@LastModifiedBy
	@Column(name = "updated_by", length = 64)
	private String updatedBy;

	@Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
	private Boolean isActive = true;
}
