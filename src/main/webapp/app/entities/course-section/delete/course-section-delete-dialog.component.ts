import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICourseSection } from '../course-section.model';
import { CourseSectionService } from '../service/course-section.service';

@Component({
  standalone: true,
  templateUrl: './course-section-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CourseSectionDeleteDialogComponent {
  courseSection?: ICourseSection;

  protected courseSectionService = inject(CourseSectionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseSectionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
