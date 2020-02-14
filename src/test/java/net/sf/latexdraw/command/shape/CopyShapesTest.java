package net.sf.latexdraw.command.shape;

import io.github.interacto.jfx.test.CommandTest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.sf.latexdraw.model.ShapeFactory;
import net.sf.latexdraw.model.api.shape.Drawing;
import net.sf.latexdraw.model.api.shape.Shape;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the command CopyShapes. Generated by Interacto test-gen.
 */
@Tag("command")
class CopyShapesTest extends CommandTest<CopyShapes> {
	Optional<SelectShapes> selection;
	List<Shape> copiedShapes;
	int nbTimeCopied;

	Shape shape;

	@Override
	protected Stream<Runnable> cannotDoFixtures() {
		return Stream.of(
			() -> cmd = new CopyShapes(Optional.empty()),
			() -> {
				final var drawing = Mockito.mock(Drawing.class);
				Mockito.when(drawing.getSelection()).thenReturn(ShapeFactory.INST.createGroup());
				cmd = new CopyShapes(Optional.of(new SelectShapes(drawing)));
			});
	}

	@Override
	protected Stream<Runnable> canDoFixtures() {
		return Stream.of(() -> {
			final var selectShapes = new SelectShapes(Mockito.mock(Drawing.class));
			shape = ShapeFactory.INST.createRectangle();
			selectShapes.setShape(shape);
			cmd = new CopyShapes(Optional.of(selectShapes));
		});
	}

	@Override
	protected Runnable doChecker() {
		return () -> {
			assertThat(cmd.copiedShapes).isNotNull();
			assertThat(cmd.copiedShapes).hasSize(1);
			assertThat(shape).isNotSameAs(cmd.copiedShapes.get(0));
		};
	}

	@AfterEach
	void tearDownCopyShapesTest() {
		this.selection = null;
		this.copiedShapes = null;
		this.nbTimeCopied = 0;
	}



	@ParameterizedTest
	@MethodSource({"canDoFixtures"})
	public void testGetSelection(final Runnable doConfig) {
		doConfig.run();
		assertThat(cmd.selection).isEqualTo(cmd.getSelection());
	}

	@ParameterizedTest
	@MethodSource({"canDoFixtures"})
	public void testUnregistered(final Runnable doConfig) {
		doConfig.run();
		assertThat(cmd.unregisteredBy(new CopyShapes(Optional.of(new SelectShapes(Mockito.mock(Drawing.class)))))).isTrue();
	}

	@ParameterizedTest
	@MethodSource({"canDoFixtures"})
	public void testUnregisteredKO(final Runnable doConfig) {
		doConfig.run();
		assertThat(cmd.unregisteredBy(null)).isFalse();
	}
}