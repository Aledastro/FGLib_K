module FGLib_K {
    requires transitive kotlin.stdlib;
    requires javafx.media;

    /////////////////////////////////////////////////////////

    exports com.uzery.fglib.core.event;

    exports com.uzery.fglib.core.obj;
    exports com.uzery.fglib.core.obj.ability;
    exports com.uzery.fglib.core.obj.bounds;
    exports com.uzery.fglib.core.obj.controller;
    exports com.uzery.fglib.core.obj.property;
    exports com.uzery.fglib.core.obj.stats;
    exports com.uzery.fglib.core.obj.visual;

    exports com.uzery.fglib.core.program;

    exports com.uzery.fglib.core.realisation;

    exports com.uzery.fglib.core.room;

    exports com.uzery.fglib.core.world;

    /////////////////////////////////////////////////////////

    exports com.uzery.fglib.utils.data.audio;
    exports com.uzery.fglib.utils.data.debug;
    exports com.uzery.fglib.utils.data.file;
    exports com.uzery.fglib.utils.data.getter;
    exports com.uzery.fglib.utils.data.getter.value;
    exports com.uzery.fglib.utils.data.image;
    exports com.uzery.fglib.utils.data.image.effects;

    exports com.uzery.fglib.utils.graphics;
    exports com.uzery.fglib.utils.graphics.data;

    exports com.uzery.fglib.utils.input;
    exports com.uzery.fglib.utils.input.data;

    exports com.uzery.fglib.utils.math;
    exports com.uzery.fglib.utils.math.geom;
    exports com.uzery.fglib.utils.math.geom.shape;
    exports com.uzery.fglib.utils.math.matrix;
    exports com.uzery.fglib.utils.math.num;
    exports com.uzery.fglib.utils.math.scale;
    exports com.uzery.fglib.utils.math.solve;

    exports com.uzery.fglib.utils.program;

    /////////////////////////////////////////////////////////
}
