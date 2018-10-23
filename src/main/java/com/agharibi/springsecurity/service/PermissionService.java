package com.agharibi.springsecurity.service;

import com.agharibi.springsecurity.model.TargetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.transaction.Transactional;

@Transactional
@Service
public class PermissionService {

    @Autowired
    private MutableAclService aclService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void addPermissionForUser(TargetEntity entity, Permission permission, String userName) {
        final Sid sid = new PrincipalSid(userName);
        addPermissionForSid(entity, permission, sid);
    }

    public void addPermissionForAuthority(TargetEntity entity, Permission permission, String authority) {
        Sid sid = new GrantedAuthoritySid(authority);
        addPermissionForSid(entity, permission, sid);
    }

    private void addPermissionForSid(TargetEntity entity, Permission permission, Sid sid) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(entity.getClass(), entity.getId());

                MutableAcl acl = null;
                try {
                    acl = (MutableAcl) aclService.readAclById(objectIdentity);
                }catch (NotFoundException e) {
                    acl = aclService.createAcl(objectIdentity);
                }

                acl.insertAce(acl.getEntries().size(), permission, sid, true);
                aclService.updateAcl(acl);
            }
        });
    }
}
