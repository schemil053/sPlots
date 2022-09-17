package de.emilschlampp.plots.commands.defaultcommands;

import de.emilschlampp.plots.commands.PlotMainCommand;
import de.emilschlampp.plots.commands.PlotSubCommand;
import de.emilschlampp.plots.commands.defaultcommands.backup.backup_command;
import de.emilschlampp.plots.commands.defaultcommands.border.editrand_command;
import de.emilschlampp.plots.commands.defaultcommands.border.editwall_command;
import de.emilschlampp.plots.commands.defaultcommands.border.rand_command;
import de.emilschlampp.plots.commands.defaultcommands.border.wall_command;
import de.emilschlampp.plots.commands.defaultcommands.chunkanalyse.analysechunks_command;
import de.emilschlampp.plots.commands.defaultcommands.chunkanalyse.tpchunk_command;
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





        register(new flag_command());
        register(new tp_command());
        register(new untrust_command());
        register(new trust_command());
        register(new setowner_command());
        register(new claim_command());
        register(new auto_command());
        register(new confirm_command());
        register(new middle_command());
        register(new clear_command());
        register(new delete_command());
        register(new home_command());
        register(new info_command());
        register(new toggle_command());
        register(new help_command());
        register(new visit_command());

        register(new rand_command());
        register(new editrand_command());
        register(new editwall_command());
        register(new wall_command());



        register(new analysechunks_command());
        register(new tpchunk_command());


        register(new backup_command());


    }




}
