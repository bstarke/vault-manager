package io.pivotal.vault.vaultmanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
public class VaultController {
    @Autowired
    private VaultTemplate vaultTemplate;

    @GetMapping("/secret")
    public ResponseEntity<String> getSecret(@RequestParam("key") String key) {
        VaultResponse data = vaultTemplate.read(key);
        if(data != null) {
            return ResponseEntity.ok().body(data.getData().toString());
        } else {
            log.debug("Not Found: " + key );
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/secret")
    public ResponseEntity<String> putSecret(@RequestBody Secret secret) {
        try {
            if(secret.getKey().startsWith("secret")) {
                vaultTemplate.write(secret.getKey(), secret.getData());
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.unprocessableEntity().body("Key does not begin with 'secret'");
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(e.getLocalizedMessage());
        }
    }

}
