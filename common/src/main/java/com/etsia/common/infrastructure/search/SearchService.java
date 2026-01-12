package com.etsia.common.infrastructure.search;

import com.etsia.common.infrastructure.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchService {

    @PersistenceContext
    private EntityManager entityManager;

    public GlobalSearchResponse globalSearch(String query, Pageable pageable) {
        String searchPattern = "%" + query.toLowerCase() + "%";
        
        List<SearchResult> users = searchUsers(searchPattern, pageable);
        List<SearchResult> posts = searchPosts(searchPattern, pageable);
        List<SearchResult> events = searchEvents(searchPattern, pageable);
        List<SearchResult> jobs = searchJobs(searchPattern, pageable);

        int total = users.size() + posts.size() + events.size() + jobs.size();

        return GlobalSearchResponse.builder()
                .users(users)
                .posts(posts)
                .events(events)
                .jobs(jobs)
                .totalResults(total)
                .query(query)
                .build();
    }

    @Cacheable(value = "search_users", key = "#query + '_' + #pageable.pageNumber")
    public List<SearchResult> searchUsers(String query, Pageable pageable) {
        String jpql = "SELECT u FROM User u WHERE " +
                "LOWER(u.name) LIKE :query OR " +
                "LOWER(u.username) LIKE :query OR " +
                "LOWER(u.bio) LIKE :query";

        TypedQuery<User> typedQuery = entityManager.createQuery(jpql, User.class)
                .setParameter("query", query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        return typedQuery.getResultList().stream()
                .map(this::mapUserToSearchResult)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "search_posts", key = "#query + '_' + #pageable.pageNumber")
    public List<SearchResult> searchPosts(String query, Pageable pageable) {
        String jpql = "SELECT p FROM Post p WHERE " +
                "LOWER(p.content) LIKE :query AND p.deletedAt IS NULL";

        TypedQuery<Post> typedQuery = entityManager.createQuery(jpql, Post.class)
                .setParameter("query", query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        return typedQuery.getResultList().stream()
                .map(this::mapPostToSearchResult)
                .collect(Collectors.toList());
    }

    public Page<SearchResult> searchPostsPaged(String query, Pageable pageable) {
        String searchPattern = "%" + query.toLowerCase() + "%";
        
        String jpql = "SELECT p FROM Post p WHERE " +
                "LOWER(p.content) LIKE :query AND p.deletedAt IS NULL";
        String countJpql = "SELECT COUNT(p) FROM Post p WHERE " +
                "LOWER(p.content) LIKE :query AND p.deletedAt IS NULL";

        TypedQuery<Post> typedQuery = entityManager.createQuery(jpql, Post.class)
                .setParameter("query", searchPattern)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        Long total = entityManager.createQuery(countJpql, Long.class)
                .setParameter("query", searchPattern)
                .getSingleResult();

        List<SearchResult> results = typedQuery.getResultList().stream()
                .map(this::mapPostToSearchResult)
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, total);
    }

    @Cacheable(value = "search_events", key = "#query + '_' + #pageable.pageNumber")
    public List<SearchResult> searchEvents(String query, Pageable pageable) {
        String jpql = "SELECT e FROM Event e WHERE " +
                "LOWER(e.title) LIKE :query OR " +
                "LOWER(e.description) LIKE :query OR " +
                "LOWER(e.category) LIKE :query OR " +
                "LOWER(e.location) LIKE :query";

        TypedQuery<Event> typedQuery = entityManager.createQuery(jpql, Event.class)
                .setParameter("query", query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        return typedQuery.getResultList().stream()
                .map(this::mapEventToSearchResult)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "search_jobs", key = "#query + '_' + #pageable.pageNumber")
    public List<SearchResult> searchJobs(String query, Pageable pageable) {
        String jpql = "SELECT j FROM JobOffer j WHERE " +
                "LOWER(j.title) LIKE :query OR " +
                "LOWER(j.description) LIKE :query OR " +
                "LOWER(j.location) LIKE :query";

        TypedQuery<JobOffer> typedQuery = entityManager.createQuery(jpql, JobOffer.class)
                .setParameter("query", query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        return typedQuery.getResultList().stream()
                .map(this::mapJobToSearchResult)
                .collect(Collectors.toList());
    }

    // Search by specific type
    public Page<SearchResult> searchByType(String query, String type, Pageable pageable) {
        String searchPattern = "%" + query.toLowerCase() + "%";
        
        return switch (type.toUpperCase()) {
            case "USER" -> searchUsersPaged(searchPattern, pageable);
            case "POST" -> searchPostsPaged(query, pageable);
            case "EVENT" -> searchEventsPaged(searchPattern, pageable);
            case "JOB" -> searchJobsPaged(searchPattern, pageable);
            default -> Page.empty(pageable);
        };
    }

    private Page<SearchResult> searchUsersPaged(String query, Pageable pageable) {
        String jpql = "SELECT u FROM User u WHERE " +
                "LOWER(u.name) LIKE :query OR " +
                "LOWER(u.username) LIKE :query OR " +
                "LOWER(u.bio) LIKE :query";
        String countJpql = "SELECT COUNT(u) FROM User u WHERE " +
                "LOWER(u.name) LIKE :query OR " +
                "LOWER(u.username) LIKE :query OR " +
                "LOWER(u.bio) LIKE :query";

        TypedQuery<User> typedQuery = entityManager.createQuery(jpql, User.class)
                .setParameter("query", query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        Long total = entityManager.createQuery(countJpql, Long.class)
                .setParameter("query", query)
                .getSingleResult();

        List<SearchResult> results = typedQuery.getResultList().stream()
                .map(this::mapUserToSearchResult)
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, total);
    }

    private Page<SearchResult> searchEventsPaged(String query, Pageable pageable) {
        String jpql = "SELECT e FROM Event e WHERE " +
                "LOWER(e.title) LIKE :query OR " +
                "LOWER(e.description) LIKE :query OR " +
                "LOWER(e.category) LIKE :query OR " +
                "LOWER(e.location) LIKE :query";
        String countJpql = "SELECT COUNT(e) FROM Event e WHERE " +
                "LOWER(e.title) LIKE :query OR " +
                "LOWER(e.description) LIKE :query OR " +
                "LOWER(e.category) LIKE :query OR " +
                "LOWER(e.location) LIKE :query";

        TypedQuery<Event> typedQuery = entityManager.createQuery(jpql, Event.class)
                .setParameter("query", query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        Long total = entityManager.createQuery(countJpql, Long.class)
                .setParameter("query", query)
                .getSingleResult();

        List<SearchResult> results = typedQuery.getResultList().stream()
                .map(this::mapEventToSearchResult)
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, total);
    }

    private Page<SearchResult> searchJobsPaged(String query, Pageable pageable) {
        String jpql = "SELECT j FROM JobOffer j WHERE " +
                "LOWER(j.title) LIKE :query OR " +
                "LOWER(j.description) LIKE :query OR " +
                "LOWER(j.location) LIKE :query";
        String countJpql = "SELECT COUNT(j) FROM JobOffer j WHERE " +
                "LOWER(j.title) LIKE :query OR " +
                "LOWER(j.description) LIKE :query OR " +
                "LOWER(j.location) LIKE :query";

        TypedQuery<JobOffer> typedQuery = entityManager.createQuery(jpql, JobOffer.class)
                .setParameter("query", query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        Long total = entityManager.createQuery(countJpql, Long.class)
                .setParameter("query", query)
                .getSingleResult();

        List<SearchResult> results = typedQuery.getResultList().stream()
                .map(this::mapJobToSearchResult)
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, total);
    }

    // Mappers
    private SearchResult mapUserToSearchResult(User user) {
        return SearchResult.builder()
                .id(user.getId())
                .type("USER")
                .title(user.getName())
                .description(user.getBio())
                .imageUrl(user.getProfilePictureUrl())
                .metadata(UserMetadata.builder()
                        .username(user.getUsername())
                        .totalFollowers(user.getTotalFollowers())
                        .totalPosts(user.getTotalPosts())
                        .isMentor(user.getIsMentor())
                        .build())
                .build();
    }

    private SearchResult mapPostToSearchResult(Post post) {
        String preview = post.getContent();
        if (preview != null && preview.length() > 200) {
            preview = preview.substring(0, 200) + "...";
        }
        
        return SearchResult.builder()
                .id(post.getId())
                .type("POST")
                .title("Post by " + (post.getUser() != null ? post.getUser().getName() : "Unknown"))
                .description(preview)
                .createdAt(post.getCreatedAt())
                .metadata(PostMetadata.builder()
                        .authorId(post.getUser() != null ? post.getUser().getId() : null)
                        .authorName(post.getUser() != null ? post.getUser().getName() : null)
                        .totalLikes(post.getTotalLikes())
                        .totalComments(post.getTotalComments())
                        .build())
                .build();
    }

    private SearchResult mapEventToSearchResult(Event event) {
        return SearchResult.builder()
                .id(event.getId())
                .type("EVENT")
                .title(event.getTitle())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .createdAt(event.getCreatedAt())
                .metadata(EventMetadata.builder()
                        .category(event.getCategory())
                        .eventDate(event.getEventDate())
                        .location(event.getLocation())
                        .maxParticipants(event.getMaxParticipants())
                        .organizerId(event.getOrganizer() != null ? event.getOrganizer().getId() : null)
                        .build())
                .build();
    }

    private SearchResult mapJobToSearchResult(JobOffer job) {
        return SearchResult.builder()
                .id(job.getId())
                .type("JOB")
                .title(job.getTitle())
                .description(job.getDescription())
                .createdAt(job.getCreatedAt())
                .metadata(JobMetadata.builder()
                        .type(job.getType() != null ? job.getType().name() : null)
                        .location(job.getLocation())
                        .salary(job.getSalary())
                        .deadline(job.getDeadline())
                        .applicationUrl(job.getApplicationUrl())
                        .publisherId(job.getPublisher() != null ? job.getPublisher().getId() : null)
                        .build())
                .build();
    }

    // Metadata classes
    @lombok.Data
    @lombok.Builder
    public static class UserMetadata {
        private String username;
        private int totalFollowers;
        private int totalPosts;
        private Boolean isMentor;
    }

    @lombok.Data
    @lombok.Builder
    public static class PostMetadata {
        private Integer authorId;
        private String authorName;
        private int totalLikes;
        private int totalComments;
    }

    @lombok.Data
    @lombok.Builder
    public static class EventMetadata {
        private String category;
        private java.time.Instant eventDate;
        private String location;
        private Integer maxParticipants;
        private Integer organizerId;
    }

    @lombok.Data
    @lombok.Builder
    public static class JobMetadata {
        private String type;
        private String location;
        private String salary;
        private java.time.Instant deadline;
        private String applicationUrl;
        private Integer publisherId;
    }
}
