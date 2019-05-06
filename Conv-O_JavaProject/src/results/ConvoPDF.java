package results;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import model.Bubble;
import model.BubbleResults;
import model.Meeting;

/**
 * 
 * @author rnich
 *
 */
public class ConvoPDF {

	private static ConvoResults results;
	private static Document doc;
	private static PdfWriter pw;
	private static PdfContentByte pcb;
	private static ArrayList<String> bubbles, attendees, missing;
	private static BaseFont fontHelv, fontHelvBold;

	public ConvoPDF(ConvoResults results) {
		this.results = results;
	}
	
	private static void setup() {

		try {
			fontHelv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
			fontHelvBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED);
			bubbles = new ArrayList<String>();
			bubbles.add("Bubble 1");
			bubbles.add("Bubble 2");
			bubbles.add("Bubble 3");
			bubbles.add("Bubble 4");

			attendees = results.getMeeting().getAttendees();
			missing = results.getMeeting().getAbsentees();

		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createPDF() {
		try {
			setup();

			doc = new Document();

			pw = PdfWriter.getInstance(doc, new FileOutputStream(
					"Meeting/" + results.getMeeting().getName() + "/Convo_" + results.getMeeting().getName() + ".pdf"));
			doc.open();

			pcb = pw.getDirectContent();
			Image img = Image.getInstance("Data/Images/PDF/titlepage.png");
			img.setAbsolutePosition(0, 0);
			pcb.addImage(img);

			doc.newPage();
			img = Image.getInstance("Data/Images/PDF/page_1.png");
			img.setAbsolutePosition(0, 0);
			pcb.addImage(img);

			addTitle();
			addAttendees();
			addAgenda();

			doc.newPage();
			img = Image.getInstance("Data/Images/PDF/page_2.png");
			img.setAbsolutePosition(0, 0);
			pcb.addImage(img);
			addBubbleRelevancyCharts();

			doc.newPage();
			img = Image.getInstance("Data/Images/PDF/page_3.png");
			img.setAbsolutePosition(0, 0);
			pcb.addImage(img);

			addLineGraph(57, 400, 527, 300);

			img = Image.getInstance("Data/Images/PDF/block.png");
			img.setAbsolutePosition(70, 400);
			pcb.addImage(img);

			// SCREENSHOT PAGE
			addTextAtPosition("End of Meeting", 310, 395, Element.ALIGN_CENTER, fontHelv, 26);
			img = Image.getInstance("Meeting/" + results.getMeeting().getName() + "/Screenshots/endOfMeeting.png");
			img.setAbsolutePosition(80, 100);
			img.setBorder(Rectangle.BOX);
			img.setBorderWidth(8);
			img.scaleAbsolute(480, 280);
			img.setUseVariableBorders(true);
			pcb.addImage(img);

			doc.close();
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void addAgenda() {
		int x = 105;
		int y = 330;
		int counter = 0;

		ArrayList<Bubble> bubbles = results.getListBubbles();

		for (int i = 0; i < bubbles.size(); i++) {

			Bubble b = bubbles.get(i);
			BubbleResults bStats = b.getResults();

			addTextAtPosition((i + 1) + ": " + b.getName(), x, y, Element.ALIGN_LEFT, fontHelvBold, 16);

			String line = "___";
			for (int z = 0; z < b.getName().length(); z++) {
				line += "_";
			}

			addTextAtPosition(line, x, y, Element.ALIGN_LEFT, fontHelvBold, 15);
			addTextAtPosition("    Duration: " + b.getTime().durationToString(), x, y - 20, Element.ALIGN_LEFT,
					fontHelv, 15);
			addTextAtPosition("      Overall: " + bStats.getBubbleStatus(), x, y - 35, Element.ALIGN_LEFT, fontHelv,
					15);

			x += 151;
			counter++;
			if (counter % 3 == 0) {
				x = 105;
				y -= 80;
			}
		}
	}

	public static String floatFormat(float f) {
		return (String.format("%.1f", f));
	}

	public static void addTitle() {

		DateFormat dateFormat = new SimpleDateFormat("dd / MM / yyyy");
		Date date = new Date();

		Meeting m = results.getMeeting();

		addTextAtPosition(m.getName(), 310, 760, Element.ALIGN_CENTER, fontHelvBold, 50);
		addTextAtPosition(m.getTime().englishDurationToString(), 30, 140, Element.ALIGN_CENTER, fontHelvBold, 20, 90);
		addTextAtPosition("Overall: " + m.getMeetingStatus(), 30, 420, Element.ALIGN_CENTER, fontHelvBold, 20, 90);
		addTextAtPosition(dateFormat.format(date), 30, 700, Element.ALIGN_CENTER, fontHelvBold, 20, 90);
	}

	public static void addTextAtPosition(String text, float x, float y, int alignment, BaseFont font, float fontSize) {
		pcb.beginText();
		pcb.setFontAndSize(font, fontSize);
		pcb.showTextAligned(alignment, text, x, y, 0);
		pcb.endText();
	}

	public static void addTextAtPosition(String text, float x, float y, int alignment, BaseFont font, float fontSize,
			float rot) {
		pcb.beginText();
		pcb.setFontAndSize(font, fontSize);
		pcb.showTextAligned(alignment, text, x, y, rot);
		pcb.endText();
	}

	public static void addBubbleRelevancyCharts() {

		int x = 75;
		int y = 580;
		int counter = 0;
		ArrayList<Bubble> bubbles = results.getListBubbles();
		ArrayList<Bubble> orderedBubbles = new ArrayList<Bubble>();

		if (bubbles.size() == 0) {
			return;
		}

		orderedBubbles.add(bubbles.get(0));

		for (int i = 1; i < bubbles.size(); i++) {
			Bubble b = bubbles.get(i);
			int size = orderedBubbles.size();
			for (int z = 0; z < size; z++) {

				if (b.getResults().getAverageRelevancy() > orderedBubbles.get(z).getResults().getAverageRelevancy()) {
					orderedBubbles.add(z, b);
					System.out.println(b.getName());
					break;
				} else if (z == orderedBubbles.size() - 1) {
					orderedBubbles.add(z, b);
					System.out.println(b.getName());
				}
			}
		}

		for (int i = 0; i < orderedBubbles.size(); i++) {
			Bubble b = orderedBubbles.get(i);
			addPieChart(x, y, 150, 100, (int) b.getResults().getOnTopicValue(),
					(int) b.getResults().getOffTopicValue());
			x += 170;
			addBubbleStats(b, x - 160, y);
			counter++;
			if (counter % 3 == 0) {
				y -= 200;
				x = 75;
			}
		}

	}

	public static void addBubbleStats(Bubble b, int x, int y) {
		y -= 10;

		BubbleResults bStats = b.getResults();

		addTextAtPosition(b.getName(), x + 70, y + 113, Element.ALIGN_CENTER, fontHelvBold, 15);

		addTextAtPosition("Average Category:", x, y - layer(0, 0), Element.ALIGN_LEFT, fontHelvBold, 10);
		addTextAtPosition(bStats.getAverageCategory(), x, y - layer(0, 1), Element.ALIGN_LEFT, fontHelv, 10);

		addTextAtPosition("Keywords:", x, y - layer(1, 2), Element.ALIGN_LEFT, fontHelvBold, 10);
		addTextAtPosition(bStats.getAverageKeywordsString(), x, y - layer(1, 3), Element.ALIGN_LEFT, fontHelv, 10);

		addTextAtPosition("Sentiment: ", x, y - layer(2, 4), Element.ALIGN_LEFT, fontHelvBold, 10);
		addTextAtPosition("                   " + bStats.getSentimentResult(), x, y - layer(2, 4), Element.ALIGN_LEFT,
				fontHelv, 10);
	}

	public static int layer(int block, int layer) {
		int blockInc = 2;
		int yInc = 14;
		return (blockInc * block) + (layer * yInc);
	}

	public static void addPieChart(int posx, int posy, int width, int height, int valx, int valy) {

		PdfTemplate template = pcb.createTemplate(width, height);

		Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
		Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);
		JFreeChart chart = generatePieChart(valx, valy);
		chart.draw(graphics2d, rectangle2d);

		pcb.addTemplate(template, posx, posy);
	}

	public static void addLineGraph(int posx, int posy, int width, int height) {

		PdfTemplate template = pcb.createTemplate(width, height);

		Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
		Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);
		JFreeChart chart = generateLineChart();
		chart.draw(graphics2d, rectangle2d);

		pcb.addTemplate(template, posx, posy);
	}

	public static JFreeChart generateLineChart() {

		Meeting m = results.getMeeting();

		ArrayList<Float> relevancePoint = m.getRelevancePoints();
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		int time = 0;

		for (Float s : relevancePoint) {
			dataSet.setValue(s, "Relevance", String.valueOf(time));
			time += 10;
		}

		JFreeChart chart = ChartFactory.createLineChart("Relevance Throughout the Meeting", "Time (s)",
				"Relevance Over Time", dataSet);
		chart.getLegend().setFrame(BlockBorder.NONE);

		return chart;
	}

	public static JFreeChart generatePieChart(int x, int y) {
		DefaultPieDataset dataSet = new DefaultPieDataset();
		dataSet.setValue("off", y);
		dataSet.setValue("on", x);
		JFreeChart chart = ChartFactory.createPieChart("", dataSet, false, false, true);
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelGenerator(null);

		return chart;
	}

	public static void addAttendees() {

		int x = 163;
		int y = 670;

		
		if (attendees.size() > 8) {
			x = 105;
		}

		int counter = 0;
		for (int i = 0; i < attendees.size(); i++) {
			addTextAtPosition(attendees.get(i), x, y - 30, Element.ALIGN_CENTER, fontHelv, 16);

			y -= 20;
			counter++;
			if (counter % 8 == 0) {
				x = 221;
				y = 670;
			}
		}

		x = 472;
		y = 670;
		counter = 0;
		for (int i = 0; i < missing.size(); i++) {
			addTextAtPosition(missing.get(i), x, y - 30, Element.ALIGN_CENTER, fontHelv, 16);

			y -= 20;
			counter++;
			if (counter % 8 == 0) {
				x += 60;
				y = 670;
			}
		}
	}

}
