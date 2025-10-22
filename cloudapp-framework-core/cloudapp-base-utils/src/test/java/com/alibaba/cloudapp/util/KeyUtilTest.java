/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.alibaba.cloudapp.util;

import com.alibaba.cloudapp.util.KeyUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class KeyUtilTest {

    @InjectMocks
    private KeyUtil keyUtil;

    private String rsaPublicKeyStr;
    private String rsaPrivateKeyStr;
    private String ecPublicKeyStr;
    private String ecPrivateKeyStr;
    @Mock
    private KeyFactory keyFactory;

    @Before
    public void setUp() {
        rsaPublicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOGCGEBKnev86i9ixW6oymhcu07tjvcexMcbseqXB65bjkAUaltMPypa+d55E1jCYkMHhkoO+gjJ0oUwvTpug/kCAwEAAQ==";
        rsaPrivateKeyStr = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEA4YIYQEqd6/zqL2LF" +
                "bqjKaFy7Tu2O9x7Exxux6pcHrluOQBRqW0w/Klr53nkTWMJiQweGSg76CMnShTC9" +
                "Om6D+QIDAQABAkAsr/YUT3UJ/b1+lGkha6tokO/BTUwmZl+CQVe5cCJih3zjSltQ" +
                "pTUC2QkNz0hcX6cuxnDi+kxXuZzZ5EN3EnQNAiEA9POzm0IceLBfqN/cIXG/TgT/" +
                "HWFOQF2TTKszlIrGu8sCIQDrreOB5/SWPXcC4P+8+34XP/PMNcikp8OmSZ2lrGeO" +
                "ywIgF1dlpQTOu71q0CfMzS8OoLufJ8iO8Vk5YRPrTVtQBrMCIQDrJLfVqQTT0ceo" +
                "S61ddN89e0VX/mGORwRiKRjtb7JzOQIhANPHLuChAksYseHDrpVmtBcqFr834Y2J" +
                "DPftXNHfiK1X";
        ecPublicKeyStr = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAECX0xhcNLcp3DHsg+OzdD/4KIhHAn" +
                "5NK9Jum2TluKwHj7OypKq78S/EXHJXzbGUzUC/aWfHwRNotkHZr6tEYCCw==";
        ecPrivateKeyStr = "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQg6zuk9LTPxuMRfLO8" +
                "+EHg1w419VI4D7HrvATXBT3EN0ahRANCAAQJfTGFw0tyncMeyD47N0P/goiEcCfk" +
                "0r0m6bZOW4rAePs7KkqrvxL8RcclfNsZTNQL9pZ8fBE2i2Qdmvq0RgIL";
    }

    @Test
    public void createPublicKey_ValidString_ReturnsRSAPublicKey() throws Exception {
        RSAPublicKey publicKey = KeyUtil.createPublicKey(rsaPublicKeyStr.getBytes(StandardCharsets.UTF_8));
        assertNotNull(publicKey);
    }

    @Test
    public void createPrivateKey_ValidString_ReturnsRSAPrivateKey() throws Exception {
        RSAPrivateKey privateKey = KeyUtil.createPrivateKey(rsaPrivateKeyStr.getBytes(StandardCharsets.UTF_8));
        assertNotNull(privateKey);
    }

    @Test
    public void createECPublicKey_ValidString_ReturnsECPublicKey() throws Exception {
        ECPublicKey publicKey = KeyUtil.createECPublicKey(ecPublicKeyStr.getBytes(StandardCharsets.UTF_8));
        assertNotNull(publicKey);
    }

    @Test
    public void createECPrivateKey_ValidString_ReturnsECPrivateKey() throws Exception {
        ECPrivateKey privateKey = KeyUtil.createECPrivateKey(ecPrivateKeyStr.getBytes(StandardCharsets.UTF_8));
        assertNotNull(privateKey);
    }
    
}
