package io.pivotal.vault.vaultmanager;

import lombok.Data;

@Data
public class Secret {
    private String key;
    private String data;
}
