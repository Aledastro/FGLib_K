/**
 * FGLib is free open-source Kotlin game library, focused on developing 2D-games
 * <p>
 * Pros:
 * <p>
 * (*) Easy to Learn, Hard to Master
 * <p>
 * FGLib provides intuitive environment to work with and it easy to build simple games in it.
 * However, it has many powerful features if you know it well
 * <p>
 * (*) Open-Source and Free
 * <p>
 * Due to it open-source nature, you can modify FGLib functionality. And it not that hard,
 * because FGLib as your project is just raw kotlin project
 * <p>
 * (*) Powerful RoomEditor
 * <p>
 * Allows you to make levels easily
 * <p>
 * (*) Technology-Independent
 * <p>
 * FGLib is technology-independent and more about philosophy about how to make games.
 * It has different realisations for different purposes,
 * such as simple JavaAwt for lightweight apps or games, that can weight around 2MB,
 * raw OpenGL (work in progress) for complex games or
 * stable JavaFX, that keeps balance with performance and complexity.
 * All works in unite environment, so you can change realisation in two lines of code.
 * Of course, you can make your own realisations as well!
 * <p>
 * Cons:
 * <p>
 * (*) Many features are still in development, because I not need them currently
 * <p>
 * (*) Lack of documentation
 * <p>
 * (*) Limited in some ways
 * <p>
 * Developer: Uzery Studio (Alexander Kandrashkin)
 * <p>
 * From one who love coding, to ones who love coding!
 * Join the community and make FGLib bigger!
 **/
module FGLib_K {
    requires kotlin.stdlib;

    /////////////////////////////////////////////////////////

    exports com.uzery.fglib.core.component;
    exports com.uzery.fglib.core.component.ability;
    exports com.uzery.fglib.core.component.bounds;
    exports com.uzery.fglib.core.component.controller;
    exports com.uzery.fglib.core.component.controller.node;
    exports com.uzery.fglib.core.component.listener;
    exports com.uzery.fglib.core.component.load;
    exports com.uzery.fglib.core.component.property;
    exports com.uzery.fglib.core.component.reaction;
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
    exports com.uzery.fglib.core.room.mask;

    exports com.uzery.fglib.core.world;
    exports com.uzery.fglib.core.world.camera;
    exports com.uzery.fglib.core.world.controller;

    /////////////////////////////////////////////////////////

    exports com.uzery.fglib.utils;

    exports com.uzery.fglib.utils.audio;

    exports com.uzery.fglib.utils.data;
    exports com.uzery.fglib.utils.data.debug;
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
