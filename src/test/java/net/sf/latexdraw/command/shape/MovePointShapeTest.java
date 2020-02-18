package net.sf.latexdraw.command.shape;

import io.github.interacto.jfx.test.UndoableCmdTest;
import java.util.List;
import java.util.stream.Stream;
import net.sf.latexdraw.model.ShapeFactory;
import net.sf.latexdraw.model.api.shape.ModifiablePointsShape;
import net.sf.latexdraw.model.api.shape.Point;
import net.sf.latexdraw.service.PreferencesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the command MovePointShape. Generated by Interacto test-gen.
 */
@Tag("command")
class MovePointShapeTest extends UndoableCmdTest<MovePointShape> {
	ModifiablePointsShape shape;
	Point point;
	Point newCoord;

	@BeforeEach
	void setUp() {
		bundle = new PreferencesService().getBundle();
	}

	private void fixtureShape() {
		shape = ShapeFactory.INST.createPolyline(List.of(
			ShapeFactory.INST.createPoint(),
			ShapeFactory.INST.createPoint(-11, 42),
			ShapeFactory.INST.createPoint(75, -50)));
	}

	@Override
	protected Stream<Runnable> canDoFixtures() {
		return Stream.of(() -> {
			fixtureShape();
			point = ShapeFactory.INST.createPoint(shape.getPtAt(1));
			newCoord = ShapeFactory.INST.createPoint(10, 20);
			cmd = new MovePointShape(shape, shape.getPtAt(1));
			cmd.setNewCoord(newCoord);
		});
	}

	@Override
	protected Stream<Runnable> cannotDoFixtures() {
		return Stream.of(
			() -> cmd = new MovePointShape(ShapeFactory.INST.createPolyline(
				List.of(ShapeFactory.INST.createPoint())), ShapeFactory.INST.createPoint(1, 2)),
			() -> cmd = new MovePointShape(ShapeFactory.INST.createPolyline(
				List.of(ShapeFactory.INST.createPoint())), ShapeFactory.INST.createPoint(1, 2)),
			() -> {
				fixtureShape();
				point = shape.getPtAt(1);
				cmd = new MovePointShape(shape, point);
				cmd.setNewCoord(ShapeFactory.INST.createPoint(10, Double.NaN));
			});
	}

	@Override
	protected Stream<Runnable> doCheckers() {
		return Stream.of(() -> {
			assertThat(shape.getPtAt(1)).isEqualTo(newCoord);
			assertThat(shape.isModified()).isTrue();
		});
	}

	@Override
	protected Stream<Runnable> undoCheckers() {
		return Stream.of(() -> {
			assertThat(shape.getPtAt(1)).isEqualTo(point);
			assertThat(shape.isModified()).isFalse();
		});
	}

	@ParameterizedTest
	@MethodSource({"canDoFixtures"})
	void testGetShape(final Runnable doConfig) {
		doConfig.run();
		assertThat(cmd.getShape()).isSameAs(shape);
	}
}
