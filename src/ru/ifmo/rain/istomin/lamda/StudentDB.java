package ru.ifmo.rain.istomin.lamda;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentDB implements StudentGroupQuery {

	@Override
	public List<Group> getGroupsByName(Collection<Student> students) {
		return students.stream().collect(Collectors.groupingBy(Student::getGroup)).entrySet().stream().map(it->new Group(it.getKey(),it.getValue().stream().sorted(Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName)).collect(
				Collectors.toList()))).sorted(Comparator.comparing(Group::getName)).collect(Collectors.toList());
	}

	@Override
	public List<Group> getGroupsById(Collection<Student> students) {
		return students.stream().collect(Collectors.groupingBy(Student::getGroup)).entrySet().stream().map(it->new Group(it.getKey(),it.getValue().stream().sorted(Student::compareTo).collect(
				Collectors.toList()))).sorted(Comparator.comparing(Group::getName)).collect(Collectors.toList());
	}

	@Override
	public String getLargestGroup(Collection<Student> students) {
		return students.stream().collect(Collectors.groupingBy(Student::getGroup)).entrySet().stream()
				.min(Comparator.comparing((Map.Entry<String, List<Student>> it) -> it.getValue().size())
						     .reversed().thenComparing(Entry::getKey)).get().getKey();
	}

	@Override
	public String getLargestGroupFirstName(Collection<Student> students) {
		return students.stream().collect(Collectors.groupingBy(Student::getGroup)).entrySet().stream()
				.min(Comparator.comparing((Map.Entry<String, List<Student>> it) -> new HashSet<>(it.getValue()).size())
						     .reversed().thenComparing(Entry::getKey)).get().getKey();
	}

	@Override
	public List<String> getFirstNames(List<Student> students) {
		return students.stream().map(Student::getFirstName).collect(Collectors.toList());
	}

	@Override
	public List<String> getLastNames(List<Student> students) {
		return students.stream().map(Student::getLastName).collect(Collectors.toList());
	}

	@Override
	public List<String> getGroups(List<Student> students) {
		return students.stream().map(Student::getGroup).collect(Collectors.toList());
	}

	@Override
	public List<String> getFullNames(List<Student> students) {
		return students.stream().map(it->it.getFirstName().concat(it.getLastName())).collect(Collectors.toList());
	}

	@Override
	public Set<String> getDistinctFirstNames(List<Student> students) {
		return students.stream().map(Student::getFirstName).collect(Collectors.toSet());
	}

	@Override
	public String getMinStudentFirstName(List<Student> students) {
		return students.stream().min(Student::compareTo).get().getFirstName();
	}

	@Override
	public List<Student> sortStudentsById(Collection<Student> students) {
		return students.stream().sorted().collect(Collectors.toList());
	}

	@Override
	public List<Student> sortStudentsByName(Collection<Student> students) {
		return students.stream().sorted(Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName)).collect(Collectors.toList());
	}

	@Override
	public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
		return students.stream().filter(it->name!=null && name.equals(it.getFirstName())).sorted(Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName)).collect(Collectors.toList());
	}

	@Override
	public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
		return students.stream().filter(it->name!=null && name.equals(it.getLastName())).sorted(Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName)).collect(Collectors.toList());
	}

	@Override
	public List<Student> findStudentsByGroup(Collection<Student> students, String group) {
		return students.stream().filter(it->group!=null && group.equals(it.getGroup())).sorted(Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName)).collect(Collectors.toList());
	}

	@Override
	public Map<String, String> findStudentNamesByGroup(Collection<Student> students, String group) {
		return students.stream().filter(it -> group != null && group.equals(it.getGroup())).collect(
				Collectors.groupingBy(Student::getLastName,
				                      Collectors.minBy(Comparator.comparing(Student::getFirstName))))
				.values().stream().map(Optional::get)
				.collect(Collectors.toMap(Student::getLastName, Student::getFirstName));
	}
}
