/*
 * Copyright (c) 2019-2020 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/GeyserConnect
 */

package org.geysermc.connect.proxy;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.common.AuthType;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connect.MasterServer;
import org.geysermc.connect.utils.Player;
import org.geysermc.connector.network.session.auth.AuthData;

public class GeyserProxySession extends GeyserSession {

    private final BedrockServerSession bedrockServerSession;
    @Getter
    private Player player;

    public GeyserProxySession(GeyserConnector connector, BedrockServerSession bedrockServerSession) {
        super(connector, bedrockServerSession);
        this.bedrockServerSession = bedrockServerSession;
    }

    @Override
    public void setAuthenticationData(AuthData authData) {
        super.setAuthenticationData(authData);

        player = MasterServer.getInstance().getTransferringPlayers().getIfPresent(authData.getXboxUUID());
        if (player == null) {
            bedrockServerSession.disconnect("Please connect to the master server and pick a server first!");
        } else {
            MasterServer.getInstance().getTransferringPlayers().invalidate(authData.getXboxUUID());
        }
    }

    @Override
    protected void disableSrvResolving() {
        // Do nothing
    }
}
