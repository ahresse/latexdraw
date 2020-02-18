package net.sf.latexdraw.command.shape;

import io.github.interacto.jfx.test.UndoableCmdTest;
import java.util.List;
import java.util.stream.Stream;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import net.sf.latexdraw.model.CompareShapeMatcher;
import net.sf.latexdraw.model.ShapeFactory;
import net.sf.latexdraw.model.api.shape.CircleArc;
import net.sf.latexdraw.model.api.shape.Dot;
import net.sf.latexdraw.model.api.shape.Drawing;
import net.sf.latexdraw.model.api.shape.Rectangle;
import net.sf.latexdraw.model.api.shape.Square;
import net.sf.latexdraw.service.PreferencesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the command PasteShapes. Generated by Interacto test-gen.
 */
@Tag("command")
class PasteShapesTest extends UndoableCmdTest<PasteShapes> {
	CopyShapes copy;
	PreferencesService prefs;
	Drawing drawing;
	Rectangle s1;
	CircleArc s2;
	Square s3;
	Dot s4;
	IntegerProperty gap;

	@BeforeEach
	void setUp() {
		bundle = new PreferencesService().getBundle();
	}

	@Override
	protected Stream<Runnable> canDoFixtures() {
		return Stream.of(() -> {
			canDoCommonFixture();
			Mockito.when(prefs.isMagneticGrid()).thenReturn(false);
			cmd = new PasteShapes(copy, prefs, drawing);
		}, () -> {
			canDoCommonFixture();
			Mockito.when(prefs.isMagneticGrid()).thenReturn(false);
			copy = new CutShapes(new SelectShapes(drawing));
			copy.nbTimeCopied = 2;
			copy.copiedShapes = List.of(drawing.getShapeAt(1).orElseThrow(), drawing.getShapeAt(3).orElseThrow());
			cmd = new PasteShapes(copy, prefs, drawing);
		}, () -> {
			canDoCommonFixture();
			Mockito.when(prefs.isMagneticGrid()).thenReturn(true);
			gap = new SimpleIntegerProperty(12);
			Mockito.when(prefs.gridGapProperty()).thenReturn(gap);
			cmd = new PasteShapes(copy, prefs, drawing);
		});
	}

	private void canDoCommonFixture() {
		drawing = ShapeFactory.INST.createDrawing();
		s1 = ShapeFactory.INST.createRectangle(ShapeFactory.INST.createPoint(202, 33), 10, 20);
		s2 = ShapeFactory.INST.createCircleArc(ShapeFactory.INST.createPoint(220, 363), 200);
		s3 = ShapeFactory.INST.createSquare(ShapeFactory.INST.createPoint(212, 353), 11);
		s4 = ShapeFactory.INST.createDot(ShapeFactory.INST.createPoint(222, 733));
		drawing.addShape(s1);
		drawing.addShape(s2);
		drawing.addShape(s3);
		drawing.addShape(s4);
		prefs = Mockito.mock(PreferencesService.class);
		copy = new CopyShapes(new SelectShapes(drawing));
		copy.nbTimeCopied = 2;
		copy.copiedShapes = List.of(drawing.getShapeAt(1).orElseThrow(), drawing.getShapeAt(3).orElseThrow());
	}

	@Override
	protected Stream<Runnable> doCheckers() {
		return Stream.of(() -> {
			final double gap = prefs.isMagneticGrid() ? -24 : -20;
			assertThat(drawing.getShapes()).hasSize(6);
			assertThat(drawing.getShapeAt(4).orElseThrow()).isInstanceOf(CircleArc.class);
			assertThat(drawing.getShapeAt(5).orElseThrow()).isInstanceOf(Dot.class);
			drawing.getShapeAt(4).orElseThrow().translate(gap, gap);
			drawing.getShapeAt(5).orElseThrow().translate(gap, gap);
			CompareShapeMatcher.INST.assertEqualsArc((CircleArc) drawing.getShapeAt(1).orElseThrow(),
				(CircleArc) drawing.getShapeAt(4).orElseThrow());
			CompareShapeMatcher.INST.assertEqualsDot((Dot) drawing.getShapeAt(3).orElseThrow(),
				(Dot) drawing.getShapeAt(5).orElseThrow());
			assertThat(drawing.isModified()).isTrue();
		});
	}

	@Override
	protected Stream<Runnable> undoCheckers() {
		return Stream.of(() -> {
			assertThat(drawing.getShapes()).hasSize(4);
			assertThat(drawing.isModified()).isFalse();
			assertThat(drawing.getShapeAt(0).orElseThrow()).isSameAs(s1);
			assertThat(drawing.getShapeAt(1).orElseThrow()).isSameAs(s2);
			assertThat(drawing.getShapeAt(2).orElseThrow()).isSameAs(s3);
			assertThat(drawing.getShapeAt(3).orElseThrow()).isSameAs(s4);
		});
	}
}
