/**
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * con terra GmbH licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.conterra.smarteditor.tiles;

import org.apache.log4j.Logger;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.Definition;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.definition.Definitions;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.impl.mgmt.CachingTilesContainer;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparer;

import javax.servlet.http.HttpServletRequest;

/**
 * This ViewPreparer prepares the <body> attribute of the tiles view depending on
 * - resource type
 * - security constraints
 * <p/>
 * @author kse
 * Date: 30.04.2010
 * Time: 23:43:53
 */
public class BodyViewPreparer implements ViewPreparer {

    private static Logger LOG = Logger.getLogger(BodyViewPreparer.class);

    /**
     * Executes before the tile view is rendered
     *
     * @param context
     * @param attributeContext
     * @throws PreparerException
     */
    public void execute(TilesRequestContext context, AttributeContext attributeContext)
            throws PreparerException {
        String lResourceType = (String) context.getRequestScope().get("resourceType");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Preparing view for resource: " + lResourceType);
        }
        if (lResourceType != null && !lResourceType.equals("")) {
            StringBuffer lBuffer = new StringBuffer();
            lBuffer.append("editor.body.");
            lBuffer.append(lResourceType);
            if (LOG.isDebugEnabled()) {
                LOG.debug("View name to add is: " + lBuffer.toString());
            }
            if (hasDefinition(lBuffer.toString(), context)) {
                LOG.info("Found valid tiles body definition");
                attributeContext.putAttribute("body", new Attribute(lBuffer.toString()));
            } else {
                LOG.info("Tiles body definition not found. Using default one");
            }
        } else {
            LOG.warn("Teste maven shade plugin: Resource type is null or empty. Using default view from 'tiles-editor.xml'.");
        }
    }

    /**
     * Checks if a definition exists with that given name
     *
     * @param pDefinitionName name of the definition to use
     * @param pContext        current tiles context
     * @return true if definition exists, otherwise false
     */
    protected boolean hasDefinition(String pDefinitionName, TilesRequestContext pContext) {
        CachingTilesContainer lContainer = (CachingTilesContainer) ((HttpServletRequest) pContext.getRequest())
                .getSession().getServletContext().getAttribute("org.apache.tiles.CONTAINER");
        try {
            Definition lDef = lContainer.getDefinitionsFactory().getDefinition(pDefinitionName, pContext);
            return lDef != null;
        } catch (DefinitionsFactoryException e) {
            LOG.error("Could not determine definition with name " + pDefinitionName);
        } catch (NullPointerException e) {
            LOG.error("Could not access definitions factory.");
        }
        return false;
    }
}
