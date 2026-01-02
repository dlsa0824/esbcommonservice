package indi.daniel.esbcommonservice.iam.controller;

import static indi.daniel.esbcommonservice.common.controller.ResponseCode.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import indi.daniel.esbcommonservice.common.controller.model.RestServiceResponse;
import indi.daniel.esbcommonservice.common.utils.JwtUtils;
import indi.daniel.esbcommonservice.iam.controller.model.req.AssetCode;
import indi.daniel.esbcommonservice.iam.controller.model.req.AuthorizeClientScopeReq;
import indi.daniel.esbcommonservice.iam.controller.model.rsp.ClientCredential;
import indi.daniel.esbcommonservice.iam.controller.model.rsp.GetJwtTokenRsp;
import indi.daniel.esbcommonservice.iam.service.IamService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/iam")
public class IamManagementController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IamService iamService;

    @PostMapping("/createClientCredential")
    public ClientCredential createClientCredential(@RequestBody @Valid AssetCode assetCode) {
        ClientCredential clientCredential = iamService.createClientCredential(assetCode.getAssetCode(), assetCode.getValidateExpiration());
        return clientCredential;
    }

    @PostMapping("/authorizeClientScope")
    public RestServiceResponse authorizeClientScope(@RequestBody @Valid AuthorizeClientScopeReq authorizeClientScopeReq) {
        iamService.authorizeClientScope(
            authorizeClientScopeReq.getClientId(),
            authorizeClientScopeReq.getClientSecret(),
            authorizeClientScopeReq.getScope());
        
        return new RestServiceResponse(SUCCESS);
    }
    
    @PostMapping("/getJwtToken")
    public GetJwtTokenRsp getJwtToken(@RequestBody @Valid AuthorizeClientScopeReq authorizeClientScopeReq) {
        String jwtToken = iamService.generateJwtToken(
            authorizeClientScopeReq.getClientId(),
            authorizeClientScopeReq.getClientSecret(),
            authorizeClientScopeReq.getScope());
        
        GetJwtTokenRsp getJwtTokenRsp = new GetJwtTokenRsp();
        getJwtTokenRsp.setJwtToken(jwtToken);

        return getJwtTokenRsp;
    }    
}
