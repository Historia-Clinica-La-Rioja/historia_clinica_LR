package ar.lamansys.sgx.shared.files.infrastructure.output.repository;

import org.springframework.stereotype.Repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

@Repository
public interface FileErrorInfoRepository extends SGXAuditableEntityJPARepository<FileErrorInfo, Long> {

}