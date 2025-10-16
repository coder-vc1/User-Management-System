package com.example.usermanagement.repository;

import com.example.usermanagement.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserSearchRepository {

    private final EntityManager entityManager;

    public UserSearchRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> fullTextSearch(String searchTerm) {
        String cleanTerm = searchTerm.trim();
        String[] searchWords = cleanTerm.split("\\s+");
        
        if (searchWords.length == 1) {
            // Single word search - check if it's a numeric ID first
            if (cleanTerm.matches("^\\d+$")) {
                try {
                    Long id = Long.parseLong(cleanTerm);
                    String jpql = "SELECT u FROM User u WHERE u.id = :id";
                    TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
                    query.setParameter("id", id);
                    List<User> result = query.getResultList();
                    if (!result.isEmpty()) {
                        return result;
                    }
                } catch (NumberFormatException e) {
                    // Continue with regular search
                }
            }
            
            // Check if it's an email pattern
            if (cleanTerm.contains("@")) {
                String jpql = "SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)";
                TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
                query.setParameter("email", cleanTerm);
                List<User> result = query.getResultList();
                if (!result.isEmpty()) {
                    return result;
                }
            }
            
            // Regular search logic
            String jpql = """
                SELECT DISTINCT u FROM User u 
                WHERE LOWER(u.firstName) LIKE LOWER(:searchTerm) 
                   OR LOWER(u.lastName) LIKE LOWER(:searchTerm) 
                   OR LOWER(u.email) LIKE LOWER(:searchTerm)
                   OR u.ssn LIKE :searchTerm
                   OR REPLACE(u.ssn, '-', '') LIKE REPLACE(:searchTermNoDash, '-', '')
                ORDER BY u.id
                """;
            
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            String searchPattern = "%" + cleanTerm + "%";
            query.setParameter("searchTerm", searchPattern);
            query.setParameter("searchTermNoDash", searchPattern);
            
            return query.getResultList();
        } else {
            // Multi-word search - analyze what fields are being searched
            List<String> nameWords = new ArrayList<>();
            List<String> ssnWords = new ArrayList<>();
            
            // Categorize search words
            for (String word : searchWords) {
                if (word.matches(".*\\d.*")) {
                    ssnWords.add(word);
                } else {
                    nameWords.add(word);
                }
            }
            
            StringBuilder jpqlBuilder = new StringBuilder("SELECT DISTINCT u FROM User u WHERE ");
            List<String> conditions = new ArrayList<>();
            
            // Handle different combinations
            if (!nameWords.isEmpty() && !ssnWords.isEmpty()) {
                // Combination search: names AND SSN
                buildNameAndConditions(jpqlBuilder, nameWords);
                jpqlBuilder.append(" AND (");
                buildSsnConditions(conditions, ssnWords);
                jpqlBuilder.append(String.join(" OR ", conditions));
                jpqlBuilder.append(")");
            } else if (nameWords.size() > 1) {
                // Multi-word name search - each word must match (AND logic)
                buildNameAndConditions(jpqlBuilder, nameWords);
            } else if (nameWords.size() == 1) {
                // Single name word - search firstName OR lastName
                String word = nameWords.get(0);
                jpqlBuilder.append("(LOWER(u.firstName) LIKE LOWER(:nameWord0) OR LOWER(u.lastName) LIKE LOWER(:nameWord0))");
            } else if (!ssnWords.isEmpty()) {
                // SSN-only search
                buildSsnConditions(conditions, ssnWords);
                jpqlBuilder.append(String.join(" OR ", conditions));
            }
            
            jpqlBuilder.append(" ORDER BY u.id");
            
            TypedQuery<User> query = entityManager.createQuery(jpqlBuilder.toString(), User.class);
            
            // Set parameters
            for (int i = 0; i < nameWords.size(); i++) {
                String wordPattern = "%" + nameWords.get(i) + "%";
                query.setParameter("nameWord" + i, wordPattern);
            }
            
            for (int i = 0; i < ssnWords.size(); i++) {
                String wordPattern = "%" + ssnWords.get(i) + "%";
                query.setParameter("ssnWord" + i, wordPattern);
            }
            
            return query.getResultList();
        }
    }
    
    private void buildNameConditions(List<String> conditions, List<String> nameWords) {
        for (int i = 0; i < nameWords.size(); i++) {
            conditions.add("LOWER(u.firstName) LIKE LOWER(:nameWord" + i + ")");
            conditions.add("LOWER(u.lastName) LIKE LOWER(:nameWord" + i + ")");
        }
    }
    
    private void buildNameAndConditions(StringBuilder jpqlBuilder, List<String> nameWords) {
        List<String> wordConditions = new ArrayList<>();
        for (int i = 0; i < nameWords.size(); i++) {
            wordConditions.add("(LOWER(u.firstName) LIKE LOWER(:nameWord" + i + ") OR LOWER(u.lastName) LIKE LOWER(:nameWord" + i + "))");
        }
        // Add full name match as OR condition
        String fullName = String.join(" ", nameWords);
        String fullNameCondition = "(LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER('%" + fullName + "%') " +
                                   "OR LOWER(CONCAT(u.lastName, ' ', u.firstName)) LIKE LOWER('%" + fullName + "%'))";
        
        jpqlBuilder.append("((").append(String.join(" AND ", wordConditions)).append(") OR ").append(fullNameCondition).append(")");
    }
    
    private void buildSsnConditions(List<String> conditions, List<String> ssnWords) {
        for (int i = 0; i < ssnWords.size(); i++) {
            conditions.add("u.ssn LIKE :ssnWord" + i);
            conditions.add("REPLACE(u.ssn, '-', '') LIKE REPLACE(:ssnWord" + i + ", '-', '')");
        }
    }

    public void indexAllUsers() {
        // No-op implementation since we're using database-based search
        // This method is kept for compatibility with the service layer
    }
}