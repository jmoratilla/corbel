package com.bq.corbel.iam.auth.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bq.corbel.iam.auth.AuthorizationRequestContext;
import com.bq.corbel.iam.auth.AuthorizationRule;
import com.bq.corbel.iam.exception.InvalidVersionException;
import com.bq.corbel.iam.exception.UnauthorizedException;
import com.bq.corbel.iam.utils.Message;
import com.github.zafarkhaja.semver.UnexpectedCharacterException;
import com.github.zafarkhaja.semver.Version;
import com.github.zafarkhaja.semver.expr.UnexpectedTokenException;

/**
 * @author Alberto J. Rubio
 */
public class VersionAuthorizationRule implements AuthorizationRule {

    private static final Logger LOG = LoggerFactory.getLogger(VersionAuthorizationRule.class);

    @Override
    public void process(AuthorizationRequestContext context) throws UnauthorizedException {
        try {
            String supportedVersions = context.getIssuerClient().getVersion();
            if (supportedVersions != null && context.hasVersion() && !Version.valueOf(context.getVersion()).satisfies(supportedVersions)) {
                throwInvalidVersionException(context);
            }
        } catch (UnexpectedCharacterException | UnexpectedTokenException e) {
            LOG.error("Client {} request has malformed version: {}", new Object[] {context.getIssuerClient().getId(), context.getVersion()});
            throwInvalidVersionException(context);
        }
    }

    private void throwInvalidVersionException(AuthorizationRequestContext context) throws InvalidVersionException {
        throw new InvalidVersionException(Message.INVALID_VERSION.getMessage(context.getVersion(), context.getIssuerClient().getVersion()));
    }
}
