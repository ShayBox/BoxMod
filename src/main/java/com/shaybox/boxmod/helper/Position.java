package com.shaybox.boxmod.helper;

import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

public class Position {

    public static void roundArgs(Args args) {
        double x = args.get(0);
        double z = args.get(2);

        x = roundPos(x);
        z = roundPos(z);

        args.set(0, x);
        args.set(2, z);
    }

    public static double roundPos(double pos) {
        pos = Math.round(pos * 100) / 100d;
        return Math.nextAfter(pos, pos + Math.signum(pos));
    }

}