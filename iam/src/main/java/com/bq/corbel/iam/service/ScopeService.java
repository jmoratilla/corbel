package com.bq.corbel.iam.service;

import java.util.Collection;
import java.util.Set;

import com.bq.corbel.iam.exception.ScopeAbsentIdException;
import com.bq.corbel.iam.exception.ScopeNameException;
import com.bq.corbel.iam.model.Scope;

/**
 * Business logic for Scope management.
 * 
 * @author Alexander De Leon
 * 
 */
public interface ScopeService {

    Scope getScope(String id);

    Set<Scope> getScopes(Collection<String> scopes);

    Set<Scope> getScopes(String... scopes);

    Set<Scope> fillScopes(Set<Scope> filledScopes, String userId, String clientId, String domainId);

    Scope fillScope(Scope scope, String userId, String clientId, String domainId);

    void addAuthorizationRules(String token, Set<Scope> filledScopes);

    void addAuthorizationRulesForPublicAccess(String token, Set<Scope> filledScopes);

    Set<Scope> expandScopes(Collection<String> scopes);

    void publishAuthorizationRules(String token, long tokenExpirationTime, Set<Scope> filledScopes);

    void create(Scope scope) throws ScopeNameException, ScopeAbsentIdException;

    void delete(String scope);


}
