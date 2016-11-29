package org.ops4j.pax.warp.core.trimou;

import java.util.HashMap;
import java.util.Map;

/**
 * To avoid reinitializing the mustache engine multiple times, this class caches the template
 * engines.
 *
 * @author Kevin Grüneberg
 */
public class TemplateEngineSelector {

    private static Map<String, TemplateEngine> templateEngines = new HashMap<>();

    public static TemplateEngine getTemplateEngine(String subprotocol) {
        if (!templateEngines.containsKey(subprotocol)) {
            initTemplateEngine(subprotocol);
        }

        return templateEngines.get(subprotocol);
    }

    private static void initTemplateEngine(String subprotocol) {
        TemplateEngine templateEngine = new TemplateEngine(subprotocol);
        templateEngines.put(subprotocol,templateEngine);
    }

}