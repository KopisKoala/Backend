package kopis.k_backend.job;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 고유 식별자

    private String jobId;
    private String status;
    private String jobType; // 작업 유형

    // 기본 생성자
    public Job() {
    }

    // 생성자
    public Job(String jobId, String status, String jobType) {
        this.jobId = jobId;
        this.status = status;
        this.jobType = jobType;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
