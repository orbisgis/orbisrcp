package org.orbisgis.core.utils;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandImageService;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IServiceLocator;
import org.orbisgis.core.logger.Logger;

public class CommandUtils {

    private static final Logger LOGGER = new Logger(CommandUtils.class);

    public static void runCommand(String commandId, IServiceLocator serviceLocator) {
        if (commandId != null) {
            try {
                ICommandService commandService = serviceLocator.getService(ICommandService.class);
                IHandlerService handlerService = serviceLocator.getService(IHandlerService.class);
                if (commandService != null) {
                    Command command = commandService.getCommand(commandId);
                    if (command != null) {
                        if (command.isEnabled()) {
                            ParameterizedCommand pc = new ParameterizedCommand(command, null);
                            handlerService.executeCommand(pc, null);
                        } else {
                            LOGGER.warn("Command '" + commandId + "' is disabled");
                        }
                    } else {
                        LOGGER.warn("Command '" + commandId + "' not found");
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Can't execute command '" + commandId + "'", e);
            }
        }
    }

    public static Image getCommandImage(String commandId) {
        ICommandImageService commandService = PlatformUI.getWorkbench().getService(ICommandImageService.class);
        if (commandService != null) {
            ImageDescriptor imageDescriptor = commandService.getImageDescriptor(commandId);
            if(imageDescriptor != null) {
                return imageDescriptor.createImage();
            }
        }
        return null;
    }
}
