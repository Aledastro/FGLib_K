module FGLib_K {
    requires kotlin.stdlib;

    /////////////////////////////////////////////////////////

    exports com.uzery.fglib.core.component;
    exports com.uzery.fglib.core.component.ability;
    exports com.uzery.fglib.core.component.bounds;
    exports com.uzery.fglib.core.component.controller;
    exports com.uzery.fglib.core.component.group;
    exports com.uzery.fglib.core.component.listener;
    exports com.uzery.fglib.core.component.reaction;
    exports com.uzery.fglib.core.component.resource;
    exports com.uzery.fglib.core.component.visual;

    exports com.uzery.fglib.core.obj;
    exports com.uzery.fglib.core.obj.event;
    exports com.uzery.fglib.core.obj.stats;

    exports com.uzery.fglib.core.program;
    exports com.uzery.fglib.core.program.data;
    exports com.uzery.fglib.core.program.extension;
    exports com.uzery.fglib.core.program.launch;

    exports com.uzery.fglib.core.realisation;
    exports com.uzery.fglib.core.realisation.packager;

    exports com.uzery.fglib.core.room;
    exports com.uzery.fglib.core.room.entry;
    exports com.uzery.fglib.core.room.mask;

    exports com.uzery.fglib.core.world;
    exports com.uzery.fglib.core.world.camera;
    exports com.uzery.fglib.core.world.controller;
    exports com.uzery.fglib.core.world.system;

    /////////////////////////////////////////////////////////

    exports com.uzery.fglib.utils;

    exports com.uzery.fglib.utils.audio;

    exports com.uzery.fglib.utils.data;
    exports com.uzery.fglib.utils.data.debug;
    exports com.uzery.fglib.utils.data.entry;
    exports com.uzery.fglib.utils.data.file;
    exports com.uzery.fglib.utils.data.getter;
    exports com.uzery.fglib.utils.data.getter.value;
    exports com.uzery.fglib.utils.data.image;
    exports com.uzery.fglib.utils.data.image.effects;

    exports com.uzery.fglib.utils.graphics;
    exports com.uzery.fglib.utils.graphics.data;
    exports com.uzery.fglib.utils.graphics.shader;

    exports com.uzery.fglib.utils.input;
    exports com.uzery.fglib.utils.input.combination;
    exports com.uzery.fglib.utils.input.data;

    exports com.uzery.fglib.utils.math.geom;
    exports com.uzery.fglib.utils.math.geom.shape;
    exports com.uzery.fglib.utils.math.matrix;
    exports com.uzery.fglib.utils.math.scale;
    exports com.uzery.fglib.utils.math.solve;

    exports com.uzery.fglib.utils.struct;
    exports com.uzery.fglib.utils.struct.num;

    /////////////////////////////////////////////////////////
}
