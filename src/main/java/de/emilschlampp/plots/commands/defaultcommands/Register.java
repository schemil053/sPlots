package de.emilschlampp.plots.commands.defaultcommands;

import de.emilschlampp.plots.commands.PlotMainCommand;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.commands.defaultcommands.backup.PlotBackupCommand;
import de.emilschlampp.plots.commands.defaultcommands.border.EditRandCommand;
import de.emilschlampp.plots.commands.defaultcommands.border.EditWallCommand;
import de.emilschlampp.plots.commands.defaultcommands.border.RandCommand;
import de.emilschlampp.plots.commands.defaultcommands.border.WallCommand;
import de.emilschlampp.plots.commands.defaultcommands.chunkanalyse.AnalyseChunksCommand;
import de.emilschlampp.plots.commands.defaultcommands.chunkanalyse.TPChunkCommand;
import de.emilschlampp.plots.commands.defaultcommands.systemcommands.*;
import de.emilschlampp.plots.utils.OfflineGetter;

public class Register {
    private void register(PlotSubCommand auto_command) {
        PlotMainCommand.register(auto_command);
    }

    public Register(PlotMainCommand command) {
        if(command.reg) {
            return;
        }
        command.reg = true;
        register();
        OfflineGetter.reg();
    }

    private void register() {





        register(new FlagCommand());
        register(new TPCommand());
        register(new UntrustCommand());
        register(new TrustCommand());
        register(new SetOwnerCommand());
        register(new ClaimCommand());
        register(new AutoCommand());
        register(new ConfirmCommand());
        register(new MiddleCommand());
        register(new ClearCommand());
        register(new DeleteCommand());
        register(new HomeCommand());
        register(new InfoCommand());
        register(new ToggleCommand());
        register(new HelpCommand());
        register(new VisitCommand());
        register(new MergeCommand());

        register(new RandCommand());
        register(new EditRandCommand());
        register(new EditWallCommand());
        register(new WallCommand());



        register(new AnalyseChunksCommand());
        register(new TPChunkCommand());


        register(new PlotBackupCommand());


    }




}
