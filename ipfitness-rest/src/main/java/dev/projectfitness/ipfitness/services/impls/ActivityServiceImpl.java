package dev.projectfitness.ipfitness.services.impls;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import dev.projectfitness.ipfitness.models.dtos.Activity;
import dev.projectfitness.ipfitness.models.dtos.ChartData;
import dev.projectfitness.ipfitness.models.entities.ActivityEntity;
import dev.projectfitness.ipfitness.models.requests.ActivityAddRequest;
import dev.projectfitness.ipfitness.repositories.ActivityEntityRepository;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.ActivityService;
import dev.projectfitness.ipfitness.util.ActionMessageResolver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.projectfitness.ipfitness.util.Constants.DEFAULT_TIMEZONE;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityEntityRepository activityEntityRepository;
    private final ModelMapper modelMapper;
    private final ActionLoggingService actionLoggingService;

    @Override
    public List<Activity> getUserActivitiesForPeriod(Integer userId, Integer month, Integer year) {
        return activityEntityRepository
                .getAllUserActivitiesForPeriodOrderDesc(userId, month, year)
                .stream()
                .map(e -> modelMapper.map(e, Activity.class))
                .collect(Collectors.toList());
    }

    @Override
    public Activity addNewActivity(ActivityAddRequest request) {
        ActivityEntity entity = modelMapper.map(request, ActivityEntity.class);
        entity.setActivityId(null);
        entity.setDatePosted(new Date());

        ActivityEntity savedEntity = activityEntityRepository.saveAndFlush(entity);
        actionLoggingService.logMessage(ActionMessageResolver.resolveAddActivity(savedEntity));
        return modelMapper.map(savedEntity, Activity.class);
    }

    @Override
    public List<ChartData> getMonthlyUserActivityData(Integer userId, Integer month, Integer year) {
        List<ChartData> activityChartData = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        activityEntityRepository
                .getAllUserActivitiesForPeriodOrderAsc(userId, month, year)
                .stream()
                .collect(Collectors.groupingBy((item) -> formatter.format(item.getDatePosted())))
                .forEach((key, value) -> {
                    ChartData data = new ChartData();
                    OptionalDouble optResult = value.stream().mapToDouble(ActivityEntity::getResult).average();
                    OptionalDouble optPercentageCompleted = value.stream().mapToInt(ActivityEntity::getPercentageCompleted).average();
                    OptionalDouble optTrainingDuration = value.stream().mapToInt(ActivityEntity::getTrainingDuration).average();
                    try {
                        calendar.setTime(formatter.parse(key));
                        data.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                        activityChartData.add(data);

                        if (optResult.isPresent())
                            data.setAvgResult(optResult.getAsDouble());
                        if (optPercentageCompleted.isPresent())
                            data.setAvgPercentageCompleted(optPercentageCompleted.getAsDouble());
                        if (optTrainingDuration.isPresent())
                            data.setAvgTrainingDuration(optTrainingDuration.getAsDouble());

                    } catch (ParseException ignored) {
                        // This case should never happen
                        // since we are already using preformatted date strings
                    }
                });
        activityChartData.sort(Comparator.comparingInt(ChartData::getDay));
        return activityChartData;
    }

    @Override
    public Resource getUserActivitiesAsPdfResource(Integer userId, Integer month, Integer year) {
        List<ActivityEntity> activities = activityEntityRepository
                .getAllUserActivitiesForPeriodOrderAsc(userId, month, year);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (Document document = new Document()) {
            PdfWriter.getInstance(document, baos);
            document.open();
            final String FONT_FAMILY = "Arial";
            Font titleFont = FontFactory.getFont(FONT_FAMILY, 22);
            Paragraph title = new Paragraph("Your fitness activity history", titleFont);
            title.setSpacingAfter(4);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Font subtitleFont = FontFactory.getFont(FONT_FAMILY, 14);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
            calendar.set(year, month - 1, 1);
            SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM");
            monthFormatter.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
            String strMonth = monthFormatter.format(calendar.getTime());
            Paragraph subtitle = new Paragraph("For " + strMonth + ", " + year + ".", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(Chunk.NEWLINE);
            SimpleDateFormat datePostedFormatter = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
            datePostedFormatter.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
            PdfPTable table = new PdfPTable(5);
            // Add PDF Table Header ->
            Stream.of("Activity Type", "Training Duration", "Percentage Completed", "Result (Weight)", "Date Posted").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                Font headFont = FontFactory.getFont(FONT_FAMILY, 12);
                header.setBackgroundColor(Color.LIGHT_GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });

            for (ActivityEntity activity : activities) {
                PdfPCell activityTypeCell = new PdfPCell(new Phrase(activity.getActivityType()));
                activityTypeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                activityTypeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(activityTypeCell);

                PdfPCell trainingDurationCell = new PdfPCell(new Phrase(activity.getTrainingDuration().toString() + "min"));
                trainingDurationCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                trainingDurationCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(trainingDurationCell);

                PdfPCell percentageCell = new PdfPCell(new Phrase(activity.getPercentageCompleted() + "%"));
                percentageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                percentageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(percentageCell);

                PdfPCell resultCell = new PdfPCell(new Phrase(activity.getResult() + "kg"));
                resultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                resultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(resultCell);

                PdfPCell datePostedCell = new PdfPCell(new Phrase(datePostedFormatter.format(activity.getDatePosted())));
                datePostedCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                datePostedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(datePostedCell);
            }
            document.add(table);
        }
        actionLoggingService.logSensitiveAction(ActionMessageResolver.resolveDownloadActivityAsPdf(userId, month, year, baos.size()));
        return new ByteArrayResource(baos.toByteArray());
    }


}
