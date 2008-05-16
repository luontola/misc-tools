/*
 * Copyright (c) 2006, Esko Luontola. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.orfjackal.tools;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Lists all available public/private key generators.
 *
 * @author Esko Luontola
 * @since 24.2.2006
 */
public class PublicPrivateKeyGeneratorsPrinter {

    private static final String ALG_ALIAS = "Alg.Alias.";

    /**
     * For more information, refer to
     * <p/>
     * http://javaalmanac.com/egs/java.security/ListServices.html
     * http://javaalmanac.com/egs/java.security/ListKeyPairGen.html
     * http://javaalmanac.com/egs/java.security/GenKeyPair.html
     * http://java.sun.com/docs/books/tutorial/security1.2/apisign/step2.html
     */
    public static void main(String[] args) {

        System.out.println("    * Available Public/Private Key Generators:");
        String[] keyPairGenerators = getCryptoImpls("KeyPairGenerator");
        Arrays.sort(keyPairGenerators);
        for (String generator : keyPairGenerators) {
            System.out.println(generator);
        }

        System.out.println();
        System.out.println("    * Available Service Types:");
        String[] serviceTypes = getServiceTypes();
        Arrays.sort(serviceTypes);
        for (String serviceType : serviceTypes) {
            System.out.println(serviceType);
        }

        System.out.println();
        System.out.println("    * Available Service Implementations by Type:");
        for (String serviceType : serviceTypes) {
            System.out.println(serviceType);
            String[] impls = getCryptoImpls(serviceType);
            Arrays.sort(impls);
            for (String impl : impls) {
                System.out.println("\t" + impl);
            }
        }
    }

    /**
     * Returns the available implementations for a service type
     */
    public static String[] getCryptoImpls(String serviceType) {
        Set<String> result = new HashSet<String>();

        // All all providers
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            // Get services provided by each provider
            Set<Object> keys = provider.keySet();
            for (Object key1 : keys) {
                String key = (String) key1;
                key = key.split(" ")[0];

                if (key.startsWith(serviceType + ".")) {
                    result.add(key.substring(serviceType.length() + 1));
                } else if (key.startsWith(ALG_ALIAS + serviceType + ".")) {
                    // This is an alias
                    result.add(key.substring(ALG_ALIAS.length() + serviceType.length() + 1));
                }
            }
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * Returns all available services types
     */
    public static String[] getServiceTypes() {
        Set<String> result = new HashSet<String>();

        // All all providers
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            // Get services provided by each provider
            Set<Object> keys = provider.keySet();
            for (Object key1 : keys) {
                String key = (String) key1;
                key = key.split(" ")[0];

                if (key.startsWith(ALG_ALIAS)) {
                    // Strip the alias
                    key = key.substring(ALG_ALIAS.length());
                }
                int ix = key.indexOf('.');
                result.add(key.substring(0, ix));
            }
        }
        return result.toArray(new String[result.size()]);
    }
}
